import { DockDemo } from "@/components/Dock";
import AddEmployeePage from "./addemployee/page";
import { Home as HomeComponent } from "@/components/Home";

import Link from "next/link";

export default function Home() {
  return (
    <div className="flex items-center justify-items-center justify-center w-full h-screen gap-2 font-[family-name:var(--font-geist-sans)]">
      <HomeComponent />

    </div>
  );
}
