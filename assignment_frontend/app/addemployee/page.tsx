"use client";

import { zodResolver } from "@hookform/resolvers/zod"; // Integrates Zod schema validation with React Hook Form
import { useForm } from "react-hook-form"; // Manages form state and validation
import { z } from "zod"; // For schema-based form validation
import { Button } from "@/components/ui/button"; // Custom Button component
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form"; // Custom Form components for consistent styling
import { Input } from "@/components/ui/input"; // Custom Input component
import { useTheme } from "next-themes"; // Manages light/dark theme
import ShineBorder from "@/components/ui/shine-border"; // Custom styled border component
import GradualSpacing from "@/components/ui/gradual-spacing"; // Custom text spacing component
import { useState } from "react"; // React state management
import Link from "next/link";

// Zod schema to validate form inputs
const formSchema = z.object({
  username: z.string().min(2, {
    message: "Employee ID must be at least 2 characters.",
  }),
  name: z.string().min(2, {
    message: "Name must be at least 2 characters.",
  }),
  ratingCategory: z.string().min(1, {
    message: "Rating is required.",
  }),
});

// Define form values based on schema
type FormValues = z.infer<typeof formSchema>;

export default function AddEmployeePage() {
  // Initialize form with validation schema and default values
  const form = useForm<FormValues>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      username: "", // Default value for Employee ID
      name: "", // Default value for Employee Name
      ratingCategory: "", // Default value for Employee Rating Category
    },
  });

  const [loading, setLoading] = useState(false); // Loading state for form submission
  const [message, setMessage] = useState<string | null>(null); // Message to display success or error feedback

  // Function to handle form submission
  const onSubmit = async (data: FormValues) => {
    setLoading(true); // Set loading state to true during submission
    setMessage(null); // Clear any previous messages
    try {
      // Send a POST request to add an employee
      const response = await fetch("http://localhost:8080/api/bell-curve/add", {
        method: "POST",
        headers: {
          "Content-Type": "application/json", // Set content type for JSON
        },
        body: JSON.stringify({
          id: data.username, // Map form data to API payload
          name: data.name,
          ratingCategory: data.ratingCategory,
        }),
      });

      if (!response.ok) {
        throw new Error("Failed to add employee"); // Throw error for non-200 response
      }

      setMessage("Employee added successfully!"); // Success message
      form.reset(); // Reset form fields
    } catch (error: any) {
      setMessage(error.message || "Something went wrong!"); // Handle error
    } finally {
      setLoading(false); // Reset loading state
    }
  };

  const theme = useTheme(); // Access theme information (currently unused)

  return (
    <div className="flex flex-col items-center justify-center w-full min-h-screen p-8 pb-20 sm:p-20 font-[family-name:var(--font-geist-sans)]">
      {/* Title */}
      <GradualSpacing
        className="font-display text-center text-2xl font-bold -tracking-widest text-black md:text-2xl md:leading-[5rem]"
        text="Add Employee"
      />

      {/* Form Container with Styled Border */}
      <ShineBorder
        className="relative flex h-[400px] w-2/3 flex-col items-center justify-center overflow-hidden rounded-lg border dark:bg-background md:shadow-xl"
        color={["#A07CFE", "#FE8FB5", "#FFBE7B"]} // Border colors
      >
        {/* Form */}
        <Form {...form}>
          <form
            onSubmit={form.handleSubmit(onSubmit)} // Form submission handler
            className="space-y-6 h-full p-4 text-left w-full text-black flex flex-col justify-between"
            aria-label="Add Employee Form" // Accessibility label
          >
            {/* Employee ID Field */}
            <FormField
              control={form.control}
              name="username"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>ID</FormLabel>
                  <FormControl>
                    <Input placeholder="Enter Employee ID" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            {/* Employee Name Field */}
            <FormField
              control={form.control}
              name="name"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Name</FormLabel>
                  <FormControl>
                    <Input placeholder="Enter Employee Name" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            {/* Rating Category Field */}
            <FormField
              control={form.control}
              name="ratingCategory"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Rating Category</FormLabel>
                  <FormControl>
                    <Input placeholder="Enter Employee Rating" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            {/* Submit Button */}
            <Button type="submit" disabled={loading}>
              {loading ? "Submitting..." : "Submit"}
            </Button>
          </form>
        </Form>

        <a className="w-[96%] m-auto flex justify-center bg-gray-950 hover:bg-[#2f2f31] text-slate-100 py-2 px-4 rounded items-center" href="/addemployee/excel"><p className="text-sm">Add Employees from Excel file</p></a>

        {/* Feedback Message */}
        {message && (
          <p
            className={`text-center mt-4 ${
              message.includes("success") ? "text-green-500" : "text-red-500"
            }`}
          >
            {message}
          </p>
        )}
      </ShineBorder>
    </div>
  );
}
