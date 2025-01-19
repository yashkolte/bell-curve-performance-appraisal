"use client";

import React, { useEffect, useState } from "react";
import { useBellCurve } from "@/contexts/BellCurveContext"; // Custom context for bell curve data
import { Component } from "@/components/Chart"; // Chart component for visualizing data
import {
  Table,
  TableBody,
  TableCaption,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"; // UI table components for structured data
import ShineBorder from "@/components/ui/shine-border"; // Styled border component
import { Deviation } from "@/components/Deviation";

const AnalyzeData: React.FC = () => {
  const { analysisData, loading, error } = useBellCurve(); // Fetch data from BellCurve context
  const [chartData, setChartData] = useState<any[]>([]); // State for chart data
  const [deviationChartData, setDeviationChartData] = useState<any[]>([]); // State for chart data

  // Map API data to chart structure whenever `analysisData` changes
  useEffect(() => {
    if (analysisData && analysisData.actualPercentage) {
      const mappedData = Object.entries(analysisData.actualPercentage).map(
        ([rating, actual], index) => {
          const standardValues = [10, 20, 40, 20, 10]; // Example standard values
          return {
            rating, // Rating category (e.g., A, B, C)
            standard: standardValues[index] || 0, // Corresponding standard value
            actual, // Actual percentage from API
          };
        }
      );
      setChartData(mappedData); // Update chart data state
    }
    console.log(analysisData); // Debugging/logging API data
  }, [analysisData]);

  useEffect(() => {
    if (analysisData && analysisData.actualPercentage) {
      const mappedData = Object.entries(analysisData.deviation).map(
        ([rating, deviation]) => {
          return {
            rating,
            deviation
          };
        }
      );
      setDeviationChartData(mappedData); // Update chart data state
    }
    console.log(analysisData); // Debugging/logging API data
  }, [analysisData]);

  // Render loading or error state
  if (loading) return <p>Loading...</p>;
  if (error) return <p>Error: {error}</p>;

  return (
    <div className="flex items-center justify-center w-9/12 h-[80vh] space-x-4 m-auto">
      {/* Chart Section */}
      <div className="w-1/3">
        <Component data={chartData} />{" "}
        {/* Pass mapped chart data to chart component */}
      </div>

      {/* Table Section */}
      <div className="w-1/3">
        <ShineBorder
          className="relative flex h-full overflow-y-auto w-full flex-col items-center justify-center overflow-hidden rounded-lg border dark:text-black dark:bg-background md:shadow-xl"
          color={["#A07CFE", "#FE8FB5", "#FFBE7B"]}
        >
          <Table className="min-h-[300px]">
            <TableCaption>Employee rating to be revised</TableCaption>
            <TableHeader>
              <TableRow>
                <TableHead>ID</TableHead><TableHead>Name</TableHead><TableHead className="text-right">Rating</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {analysisData?.adjustments.map((adjustments) => (
                <TableRow key={adjustments.id}><TableCell className="font-medium">{adjustments.id}</TableCell><TableCell>{adjustments.name}</TableCell><TableCell className="text-right">{adjustments.ratingCategory}</TableCell></TableRow>
              ))}
            </TableBody>
          </Table>
        </ShineBorder>
      </div>
      <div className="w-1/3">
      <Deviation data={deviationChartData} />
      </div>
    </div>
  );
};

export default AnalyzeData;
