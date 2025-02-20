import { For, Show } from 'solid-js';

export type Options<T> = { name: T; display: string }[];

export interface Props<T extends string> {
    callback: (selection: T) => void;
    value: T;
    options?: Options<T>;
    placeholder: string;
}

export default function <T extends string>(props: Props<T>) {
    return (
        <Show when={props.options}>
            <select
                value={props.value}
                onChange={(e) => {
                    props.callback(e.target.value as T);
                }}
                required
            >
                <option value="" disabled>
                    {props.placeholder}
                </option>

                <For each={props.options}>{(option) => <option value={option.name}>{option.display}</option>}</For>
            </select>
        </Show>
    );
}
