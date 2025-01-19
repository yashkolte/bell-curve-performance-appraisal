"use client";

import { useForm } from "react-hook-form";
import * as XLSX from "xlsx";
import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"

export default function AddEmployeePage() {
  const { handleSubmit, register, reset } = useForm();
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState<string | null>(null);

  const onSubmit = async (data: any) => {
    setLoading(true);
    setMessage(null);

    try {
      const file = data.file[0]; // Get the uploaded file
      const fileData = await file.arrayBuffer(); // Read the file as an ArrayBuffer
      const workbook = XLSX.read(fileData, { type: "array" }); // Parse the workbook
      const sheet = workbook.Sheets[workbook.SheetNames[0]]; // Get the first sheet
      const rows = XLSX.utils.sheet_to_json(sheet); // Convert the sheet to JSON

      // Map Excel rows to the required format
      const employees = rows.map((row: any) => ({
        id: row.ID, // Adjust based on your Excel column headers
        name: row.Name,
        ratingCategory: row.RatingCategory,
      }));

      // Send the JSON data to the backend
      const response = await fetch("http://localhost:8080/api/bell-curve/add-all", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(employees),
      });

      if (!response.ok) {
        throw new Error("Failed to upload employees");
      }

      setMessage("Employees uploaded successfully!");
      reset(); // Reset the form
    } catch (error: any) {
      setMessage(error.message || "Something went wrong!");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex flex-col items-center justify-center w-full min-h-screen p-8 pb-20 sm:p-20 font-[family-name:var(--font-geist-sans)]">
      <h1 className="text-2xl font-bold mb-6">Upload Employee Data</h1>
      <form
        onSubmit={handleSubmit(onSubmit)}
        className="space-y-6 w-1/2 flex flex-col items-center"
      >
        {/* File Upload */}
        <div className="grid w-full max-w-sm items-center gap-1.5">
      <Label htmlFor="excel">Please Upload Excel File</Label>
      <Input id="excel" type="file" 
          {...register("file", { required: true })}
          accept=".xlsx, .xls"/>

    </div>
        <Button type="submit" disabled={loading}>
          {loading ? "Uploading..." : "Upload"}
        </Button>
      </form>
      {message && (
        <p
          className={`mt-4 ${
            message.includes("success") ? "text-green-500" : "text-red-500"
          }`}
        >
          {message}
        </p>
      )}
    </div>
  );
}
