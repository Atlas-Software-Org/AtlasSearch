import { createEffect, createSignal, type JSX, Show, untrack, useContext, type Accessor } from 'solid-js';
import { createQueryKey } from './QueryController';
import type { LoadingInterface } from '../loading/Loading';
import Loading, { LoadingContext } from '../loading/Loading';

export function withQuery<T>(f: () => Promise<T>, loading: LoadingInterface, resetting: boolean, c?: (t: T) => void) {
    const reset = () => {
        if (resetting) {
            loading.setLoading(false);
        }
    };

    loading.setLoading(true);
    f()
        .then((t) => {
            if (c) {
                c(t);
            }
            return t;
        })
        .catch((e) => {
            loading.setError(String(e));
            throw e;
        })
        .finally(reset);
}

export function createQuery<T>(f: () => Promise<T>, queryKey?: string): [Accessor<T | undefined>, Accessor<boolean>] {
    const loading = useContext(LoadingContext);
    const [resolved, setResolved] = createSignal(false);
    const [result, setResult] = createSignal<T | undefined>(undefined);

    const run = () => {
        withQuery(f, loading, true, (t) => {
            setResult(() => t);
            setResolved(true);
        });
    };

    createEffect(run);
    createQueryKey(queryKey, run);

    return [result, resolved];
}

export interface Props<T> {
    f: () => Promise<T>;
    queryKey?: string;
    children: (item: T) => JSX.Element;
}

function Wrapped<T>(props: Props<T>) {
    const [data, resolved] = createQuery(
        untrack(() => props.f),
        untrack(() => props.queryKey)
    );
    return <Show when={resolved()}>{props.children(data()!)}</Show>;
}

export default function <T>(props: Props<T> & { inlineLoading?: boolean }) {
    return (
        <Loading initial={true} inline={props.inlineLoading ?? false}>
            <Wrapped {...props} />
        </Loading>
    );
}
