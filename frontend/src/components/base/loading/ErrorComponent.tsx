import Overlay from '../generic/Overlay';

export interface Props {
    error: string;
}

export default function (props: Props) {
    return (
        <Overlay visible>
            <div class="field">
                <p>{props.error}</p>
                <button class="button" onClick={() => location.reload()}>
                    Reload
                </button>
            </div>
        </Overlay>
    );
}
