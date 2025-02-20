import { createContext, DEV, type JSX, onCleanup, onMount, useContext } from 'solid-js';
import { isServer } from 'solid-js/web';

export type QueryRunner = () => void;

export interface QueryContextI {
    refetch: (key?: string) => void;
    register: (key: string, run: QueryRunner) => void;
    unregister: (key: string) => void;
}

export const QueryContext = createContext<QueryContextI>({
    refetch: (key) => console.warn(`QueryContext.refetch(${key})`),
    register: (key) => console.warn(`QueryContext.register(${key}, ...)`),
    unregister: (key) => console.warn(`QueryContext.unregister(${key})`),
});

export function createQueryKey(queryKey: string | undefined, run: QueryRunner) {
    const query = useContext(QueryContext)!;

    onCleanup(() => {
        if (queryKey && !isServer) {
            query.unregister(queryKey);
        }
    });
    onMount(() => {
        if (queryKey && !isServer) {
            query.register(queryKey, run);
        }
    });
}

export default function (props: { children: JSX.Element }) {
    const runners = new Map<string, QueryRunner>();

    const refetch = (key?: string) => {
        if (key) {
            const runner = runners.get(key);
            if (runner) {
                if (DEV) console.log('[#] query key ' + key);
                runner();
            }
        } else {
            for (const runner of runners.values()) {
                runner();
            }
        }
    };

    const register = (key: string, run: QueryRunner) => {
        if (runners.has(key)) {
            throw new Error('Duplicate query key ' + key);
        }
        runners.set(key, run);
        if (DEV) console.log('[+] query key ' + key);
    };

    const unregister = (key: string) => {
        runners.delete(key);
        if (DEV) console.log('[-] query key ' + key);
    };

    return <QueryContext.Provider value={{ refetch, register, unregister }}>{props.children}</QueryContext.Provider>;
}
