import { type JSX } from 'solid-js';
import Overlay from './Overlay';

export interface Props {
    continueCallback: () => void;
    cancelCallback: () => void;
    resetCallback: () => void;
    children: JSX.Element;
    visible: boolean;
}

export default function (props: Props) {
    return (
        <Overlay visible={props.visible}>
            <div class="field">
                {props.children}
                <div class="section">
                    <button
                        class="button w-2/5"
                        onClick={() => {
                            props.resetCallback();
                            props.continueCallback();
                        }}
                    >
                        Continue
                    </button>
                    <button
                        class="button w-2/5"
                        onClick={() => {
                            props.resetCallback();
                            props.cancelCallback();
                        }}
                    >
                        Cancel
                    </button>
                </div>
            </div>
        </Overlay>
    );
}
