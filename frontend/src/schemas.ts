import { z } from 'zod';

export const userConfigSchema = z.object({
    shouldKeepHistory: z.boolean(),
});
export type UserConfig = z.infer<typeof userConfigSchema>;

export const userSchema = z.object({
    username: z.string(),
    profilePictureUrl: z.string(),
    isAdministrator: z.boolean(),
    isPremiumUser: z.boolean(),
    configuration: userConfigSchema,
});
export type User = z.infer<typeof userSchema>;

export const searchResultSchema = z.object({
    link: z.string().url(),
    title: z.string().nullish(),
    description: z.string().nullish(),
    shortText: z.string().nullish(),
    score: z.number().nullish(),
});
export type SearchResult = z.infer<typeof searchResultSchema>;

export const searchResultsSchema = z.array(searchResultSchema);
export type SearchResults = z.infer<typeof searchResultSchema>;
