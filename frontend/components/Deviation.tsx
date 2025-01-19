"use client"

import { TrendingUp } from "lucide-react"
import { Area, AreaChart, CartesianGrid, XAxis } from "recharts"

import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card"
import {
  ChartConfig,
  ChartContainer,
  ChartTooltip,
  ChartTooltipContent,
} from "@/components/ui/chart"

const chartConfig = {
  deviation: {
    label: "Deviation",
    color: "hsl(var(--chart-1))",
  },
} satisfies ChartConfig

export function Deviation({ data }: { data: any[] }) {
  return (
    <Card className="">
      <CardHeader>
        <CardTitle>Deviation Chart</CardTitle>
        <CardDescription>
        Deviation = Actual − Standard Percentage
        </CardDescription>
      </CardHeader>
      <CardContent>
        <ChartContainer config={chartConfig}>
          <AreaChart
            accessibilityLayer
            data={data}
            margin={{
              left: 12,
              right: 12,
            }}
          >
            <CartesianGrid vertical={false} />
            <XAxis
              dataKey="rating"
              tickLine={false}
              axisLine={false}
              tickMargin={8}
              tickFormatter={(value) => value.slice(0, 3)}
            />
            <ChartTooltip
              cursor={false}
              content={<ChartTooltipContent indicator="line" />}
            />
            <Area
              dataKey="deviation"
              type="natural"
              fill="var(--color-deviation)"
              fillOpacity={0.4}
              stroke="var(--color-deviation)"
            />
          </AreaChart>
        </ChartContainer>
      </CardContent>
    </Card>
  )
}
