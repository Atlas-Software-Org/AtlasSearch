import { createContext, createSignal, type JSX, Show } from 'solid-js';
import LoadingSpinner from './LoadingSpinner';
import ErrorComponent from './ErrorComponent';

export interface LoadingInterface {
    setLoading: (loading: boolean) => void;
    setError: (error: string) => void;
    setProgress: (progress: number) => void;
}

export const LoadingContext = createContext<LoadingInterface>({
    setLoading: (loading: boolean) => console.warn('setLoading', loading),
    setError: (error: string) => console.warn('setError', error),
    setProgress: (progress: number) => console.warn('setProgress', progress),
});

export interface Props {
    children: JSX.Element;
    initial: boolean;
    inline: boolean;
    progressBar?: boolean;
}

export default function (props: Props) {
    const [loading, setLoadingRaw] = createSignal(props.initial);
    const [progress, setProgress] = createSignal(0);
    const [error, setError] = createSignal<string>();

    const setLoading = (loading: boolean) => {
        setLoadingRaw(loading);
        if (!loading) {
            setProgress(0);
        }
    };

    return (
        <LoadingContext.Provider value={{ setLoading, setError, setProgress }}>
            <Show
                when={error()}
                fallback={
                    <>
                        <LoadingSpinner visible={loading()} inline={props.inline}>
                            <Show when={props.progressBar}>
                                <div class="h-3 w-[10vw] rounded-full bg-gray-200">
                                    <div class="h-3 rounded-full bg-blue-600" style={{ width: `${progress()}%` }}></div>
                                </div>
                                <div class="center">{Math.floor(progress())}%</div>
                            </Show>
                        </LoadingSpinner>

                        <div style={{ display: loading() ? 'none' : 'block' }}>{props.children}</div>
                    </>
                }
            >
                <ErrorComponent error={error()!} />
            </Show>
        </LoadingContext.Provider>
    );
}
