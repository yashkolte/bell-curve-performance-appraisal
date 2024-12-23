import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import "./globals.css";
import {DockDemo} from "../components/Dock";
import { BellCurveProvider } from "../contexts/BellCurveContext";
import { cn } from "@/lib/utils";
import { DotPattern } from "@/components/ui/dot-pattern";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "Assignment",
  description: "Assignment Project of Estuate Software",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body
        className={`${geistSans.variable} ${geistMono.variable} antialiased`}
      >
        <BellCurveProvider>
        {children}
        <DockDemo />
        <DotPattern
        className={cn(
          "[mask-image:radial-gradient(400px_circle_at_center,white,transparent)]","-z-10","absolute","w-full","h-full","bg-gradient-to-br","from-[#A07CFE]","to-[#FE8FB5]","dark:from-[#1A202C]","dark:to-[#2D3748]","dark:bg-dark-background"
        )}
      />
        </BellCurveProvider>
   
      </body>
    </html>
  );
}
