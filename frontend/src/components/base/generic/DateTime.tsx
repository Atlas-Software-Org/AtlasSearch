import { createEffect, createSignal } from 'solid-js';

export interface Props {
    callback: (date: Date) => void;
}

export default function (props: Props) {
    const [date, setDate] = createSignal('');
    const [time, setTime] = createSignal('');

    createEffect(() => {
        if (date() && time()) {
            const dateTime = new Date(`${date()}T${time()}`);
            props.callback(dateTime);
        }
    });

    return (
        <div>
            <input type="date" required onChange={(e) => setDate(e.target.value)} />
            <input type="time" required onChange={(e) => setTime(e.target.value)} />
        </div>
    );
}
