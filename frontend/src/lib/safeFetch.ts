import { type ZodSchema } from 'zod';

export function validateOrThrow<T>(schema: ZodSchema<T>, data: unknown): T {
    const result = schema.safeParse(data);
    if (!result.success) {
        console.log(result.error);
        throw new Error('Field validation failed');
    }
    return result.data;
}

export async function saveFetch(input: RequestInfo | URL, init?: RequestInit): Promise<Response>;
export async function saveFetch<T>(input: RequestInfo | URL, init: RequestInit, schema: ZodSchema<T>): Promise<T>;
export async function saveFetch<T>(input: RequestInfo | URL, init?: RequestInit, schema?: ZodSchema<T>): Promise<Response | T> {
    const response = await fetch(input, init);
    if (!response.ok) {
        let errorMessage = '';
        try {
            const data = await response.json();
            errorMessage = data.message ?? 'Fetch failed';
        } catch (ignored) {
            throw new Error('Fetch failed');
        }

        throw new Error(errorMessage);
    }
    if (schema) {
        const data = await response.json();
        return validateOrThrow(schema, data);
    }
    return response;
}

// export async function fetchAndValidate<T>(schema: ZodSchema<T>, f: () => Promise<Response>): Promise<T> {
//     // await new Promise((resolve) => setTimeout(resolve, 1000));
//     const response = await f();
//     const data = await response.json();
//     return validateOrThrow(schema, data);
// }
