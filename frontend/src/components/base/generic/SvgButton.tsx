export interface Props {
    callback: () => void;
    src: string;
}

export default function (props: Props) {
    return (
        <span onClick={() => props.callback()}>
            <img src={props.src} class="h-4" />
        </span>
    );
}
