import { type JSX, Show } from 'solid-js';

export interface Props {
    children: JSX.Element;
    visible: boolean;
}

export default function (props: Props) {
    return (
        <Show when={props.visible}>
            <div class="fixed inset-0 z-10! h-full w-full bg-zinc-800/60">
                <div class="fixed inset-0 m-auto h-fit w-1/2 max-sm:w-4/5">{props.children}</div>
            </div>
        </Show>
    );
}
