// @ts-check
import { defineConfig } from 'astro/config';
import tailwindcss from '@tailwindcss/vite';
import solidJs from '@astrojs/solid-js';

import sitemap from '@astrojs/sitemap';

// https://astro.build/config
export default defineConfig({
    site: 'https://search.atlas-software.org',
    integrations: [
        solidJs(),
        sitemap({
            filter: (route) => !route.includes('/admin'),
        }),
    ],
    vite: { plugins: [tailwindcss()] },
});
