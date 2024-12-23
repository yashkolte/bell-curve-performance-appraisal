 "use client";
 import React, { createContext, useContext, useState, useEffect } from "react";
 import axios from "axios";

export interface Employee {
    id: number;
    name: string;
    ratingCategory: string;
  }
  
  export interface AnalysisData {
    actualPercentage: Record<string, number>;
    deviation: Record<string, number>;
    adjustments: Employee[];
  }
  
  export interface BellCurveContextType {
    analysisData: AnalysisData | null;
    employees: Employee[];
    loading: boolean;
    error: string | null;
    addEmployee: (newEmployee: Omit<Employee, "id">) => Promise<void>;
  }

const BellCurveContext = createContext<BellCurveContextType | undefined>(undefined);

export const BellCurveProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [analysisData, setAnalysisData] = useState<AnalysisData | null>(null);
  const [employees, setEmployees] = useState<Employee[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [analysisRes, employeesRes] = await Promise.all([
          axios.get<AnalysisData>("http://localhost:8080/api/bell-curve/analyze"),
          axios.get<Employee[]>("http://localhost:8080/api/bell-curve/employees"),
        ]);
        setAnalysisData(analysisRes.data);
        setEmployees(employeesRes.data);
      } catch (err: any) {
        setError(err.message || "Something went wrong");
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  const addEmployee = async (newEmployee: Omit<Employee, "id">) => {
    try {
      const response = await axios.post<Employee>("http://localhost:8080/api/bell-curve/add", newEmployee, {
        headers: { "Content-Type": "application/json" },
      });
      setEmployees((prev) => [...prev, response.data]);
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <BellCurveContext.Provider value={{ analysisData, employees, loading, error, addEmployee }}>
      {children}
    </BellCurveContext.Provider>
  );
};

export const useBellCurve = (): BellCurveContextType => {
  const context = useContext(BellCurveContext);
  if (!context) {
    throw new Error("useBellCurve must be used within a BellCurveProvider");
  }
  return context;
};
