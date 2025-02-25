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

export const uploadResultSchema = z.object({
    url: z.string().url(),
    uploadToken: z.string(),
    id: z.string(),
});
export type UploadResult = z.infer<typeof uploadResultSchema>;

export const timingAverageEntrySchema = z.object({
    timing: z.number(),
    date: z.string(),
});
export type TimingAverageEntry = z.infer<typeof timingAverageEntrySchema>;

export const timingsAveragesSchema = z.record(z.array(timingAverageEntrySchema));
export type TimingsAverages = z.infer<typeof timingsAveragesSchema>;

export const crawlResultSchema = z.object({
    status: z.enum(['INSERTED', 'UPDATED', 'REJECTED', 'FAILED']),
});
export type CrawlResult = z.infer<typeof crawlResultSchema>;

export const crawlHistoryEntrySchema = z.object({
    username: z.string(),
    url: z.string().url(),
    id: z.number(),
    timestamp: z.number(),
    status: z.enum(['INSERTED', 'UPDATED', 'REJECTED', 'FAILED', 'unknown']),
});
export type CrawlHistoryEntry = z.infer<typeof crawlHistoryEntrySchema>;

export const crawlHistorySchema = z.array(crawlHistoryEntrySchema);
// export type CrawlHistory = z.infer<typeof crawlHistorySchema>;
