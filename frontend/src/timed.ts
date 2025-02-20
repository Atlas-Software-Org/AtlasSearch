export function timed<T>(f: () => Promise<T>, timeKey: string): Promise<[T, number]> {
    const start = Date.now();
    return f().then((result) => {
        const time = Date.now() - start;
        console.log(`Time taken for ${timeKey}: ${time}ms`);

        fetch('/api/v1/timing', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                key: timeKey,
                time,
            }),
        });

        return [result, time];
    });
}
