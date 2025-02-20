import Overlay from '../generic/Overlay';

import loadingSvg from '../../../assets/loading.svg?url';
import { Match, Show, Switch, type JSX } from 'solid-js';

export interface Props {
    visible: boolean;
    inline: boolean;
    children?: JSX.Element;
}

export default function (props: Props) {
    return (
        <Switch>
            <Match when={props.inline}>
                <Show when={props.visible}>
                    <img src={loadingSvg} class="w-8 animate-spin" />
                    {props.children}
                </Show>
            </Match>
            <Match when={!props.inline}>
                <Overlay visible={props.visible}>
                    <div class="flex flex-col items-center">
                        <img src={loadingSvg} class="w-8 animate-spin" />
                        {props.children}
                    </div>
                </Overlay>
            </Match>
        </Switch>
    );
}
