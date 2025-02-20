import { type ZodSchema } from 'zod';

export function validateOrThrow<T>(schema: ZodSchema<T>, data: unknown): T {
    const result = schema.safeParse(data);
    if (!result.success) {
        console.log(result.error);
        throw new Error('Field validation failed');
    }
    return result.data;
}

export async function fetchAndValidate<T>(schema: ZodSchema<T>, f: () => Promise<Response>): Promise<T> {
    // await new Promise((resolve) => setTimeout(resolve, 1000));
    const response = await f();
    const data = await response.json();
    return validateOrThrow(schema, data);
}
