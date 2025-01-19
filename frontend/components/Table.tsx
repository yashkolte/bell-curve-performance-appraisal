"use client";

import { useEffect, useState } from "react";
import {
  Table,
  TableBody,
  TableCaption,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import ShineBorder from "./ui/shine-border";

type Employee = {
  id: number;
  name: string;
  ratingCategory: string;
};

export function TableDemo() {
  const [employees, setEmployees] = useState<Employee[]>([]); // State to hold employees data
  const [loading, setLoading] = useState<boolean>(true); // State for loading status
  const [error, setError] = useState<string | null>(null); // State for error message

  useEffect(() => {
    // Fetch data from your backend API
    const fetchEmployees = async () => {
      try {
        const response = await fetch("http://localhost:8080/api/bell-curve/employees"); // Replace with your actual endpoint
        if (!response.ok) {
          throw new Error("Failed to fetch employees");
        }
        const data = await response.json();
        setEmployees(data); // Update state with fetched data
      } catch (err: any) {
        setError(err.message); // Set error message if the fetch fails
      } finally {
        setLoading(false); // Set loading to false after the fetch is complete
      }
    };

    fetchEmployees(); // Call the fetch function on component mount
  }, []); // Empty dependency array ensures it runs once when the component mounts

  return (
    <div className="flex flex-col items-center justify-center w-full  p-6 sm:p-6 font-[family-name:var(--font-geist-sans)]">
      {loading && <p>Loading...</p>}
      {error && <p className="text-red-500">{error}</p>} {/* Display error if it occurs */}
      {!loading && !error && (
        <ShineBorder
          className="relative flex overflow-y-auto h-80 w-2/3 flex-col items-center justify-center overflow-hidden rounded-lg border dark:text-black dark:bg-background md:shadow-xl"
          color={["#A07CFE", "#FE8FB5", "#FFBE7B"]}
        >
          <Table className="min-h-[120px]">
            <TableCaption>A list of all employees.</TableCaption>
            <TableHeader>
              <TableRow>
                <TableHead>ID</TableHead>
                <TableHead>Name</TableHead>
                <TableHead className="text-right">Rating</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {employees.map((employees) => (
                <TableRow key={employees.id}>
                  <TableCell className="font-medium">{employees.id}</TableCell>
                  <TableCell>{employees.name}</TableCell>
                  <TableCell className="text-right">{employees.ratingCategory}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </ShineBorder>
      )}
    </div>
  );
}
