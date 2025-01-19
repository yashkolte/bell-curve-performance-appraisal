"use client";

import { cva, type VariantProps } from "class-variance-authority";
import {
  motion,
  MotionProps,
  MotionValue,
  useMotionValue,
  useSpring,
  useTransform,
} from "motion/react";
import React, { useRef } from "react";
import { cn } from "@/lib/utils";

const DEFAULT_SIZE = 40;
const DEFAULT_MAGNIFICATION = 60;
const DEFAULT_DISTANCE = 140;

const dockVariants = cva(
  "supports-backdrop-blur:bg-white/10 supports-backdrop-blur:dark:bg-black/10 mx-auto mt-8 flex h-[58px] w-max items-center justify-center gap-2 rounded-2xl border p-2 backdrop-blur-md",
);

interface DockProps extends VariantProps<typeof dockVariants> {
  className?: string;
  direction?: "top" | "middle" | "bottom";
  children: React.ReactNode;
}

interface DockIconProps extends Omit<MotionProps & React.HTMLAttributes<HTMLDivElement>, "children"> {
  className?: string;
  children?: React.ReactNode;
}

const DockIcon = ({ className, children, ...props }: DockIconProps) => {
  const ref = useRef<HTMLDivElement>(null);
  const size = DEFAULT_SIZE;
  const magnification = DEFAULT_MAGNIFICATION;
  const distance = DEFAULT_DISTANCE;
  const padding = Math.max(6, size * 0.2);
  
  const mouseX = useMotionValue(Infinity);
  
  const distanceCalc = useTransform(mouseX, (val: number) => {
    const bounds = ref.current?.getBoundingClientRect() ?? { x: 0, width: 0 };
    return val - bounds.x - bounds.width / 2;
  });

  const sizeTransform = useTransform(
    distanceCalc,
    [-distance, 0, distance],
    [size, magnification, size],
  );

  const scaleSize = useSpring(sizeTransform, {
    mass: 0.1,
    stiffness: 150,
    damping: 12,
  });

  return (
    <motion.div
      ref={ref}
      style={{ width: scaleSize, height: scaleSize, padding }}
      className={cn(
        "flex aspect-square cursor-pointer items-center justify-center rounded-full",
        className
      )}
      onMouseMove={(e) => mouseX.set(e.pageX)}
      onMouseLeave={() => mouseX.set(Infinity)}
      {...props}
    >
      {children}
    </motion.div>
  );
};

const Dock = React.forwardRef<HTMLDivElement, DockProps>(
  ({ className, children, direction = "middle", ...props }, ref) => {
    return (
      <div
        ref={ref}
        className={cn(dockVariants({ className }), {
          "items-start": direction === "top",
          "items-center": direction === "middle",
          "items-end": direction === "bottom",
        })}
        {...props}
      >
        {children}
      </div>
    );
  }
);

Dock.displayName = "Dock";
DockIcon.displayName = "DockIcon";

export { Dock, DockIcon, dockVariants };