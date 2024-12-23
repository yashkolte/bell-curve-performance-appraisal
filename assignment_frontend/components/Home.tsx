import React from 'react'
import { TableDemo } from '@/components/Table'
import GradualSpacing from './ui/gradual-spacing'
import { RainbowButton } from "@/components/ui/rainbow-button";
import Link from 'next/link';

export function Home() {
    return (
        <div className='w-full flex flex-col align-middle items-center justify-center'>
            <GradualSpacing
                className="font-display text-center text-2xl font-bold -tracking-widest text-black md:text-2xl"
                text="Employee List"
            />
            <TableDemo />
            <Link href="/analyze"><RainbowButton>Analyze</RainbowButton></Link>
        </div>
    )
}