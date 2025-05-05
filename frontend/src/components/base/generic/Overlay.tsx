import { type JSX, Show } from 'solid-js';

export interface Props {
    children: JSX.Element;
    visible: boolean;
    reset: () => void;
}

export default function (props: Props) {
    return (
        <Show when={props.visible}>
            <div class="fixed inset-0 z-20 h-screen w-screen bg-neutral-800/60  flex items-center justify-center" onClick={props.reset}>
                <div class="fixed inset-0 z-20 m-auto w-1/2 max-sm:w-4/5 top-1/2" onClick={(e) => e.stopPropagation()}>
                    {props.children}
                </div>
            </div>
        </Show>
    );
}
