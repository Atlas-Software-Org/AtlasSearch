import { useContext } from 'solid-js';
import Loading, { LoadingContext } from '../base/loading/Loading';
import { fetchAndValidate } from '../../validatedFetch';
import { uploadResultSchema } from '../../schemas';
import { timed } from '../../timed';
import { QueryContext } from '../base/query/QueryController';

async function readAsDataURL(file: File): Promise<string> {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.onload = () => {
            resolve(reader.result as string);
        };
        reader.onerror = reject;
        reader.readAsDataURL(file);
    });
}

async function squareImage(file: File): Promise<Blob> {
    const dataURL = await readAsDataURL(file);
    return new Promise((resolve, reject) => {
        const img = new Image();
        img.onload = () => {
            const size = Math.max(img.width, img.height);

            const canvas = document.createElement('canvas');
            canvas.width = size;
            canvas.height = size;

            const ctx = canvas.getContext('2d');
            if (!ctx) {
                reject(new Error('Could not create canvas'));
                return;
            }

            ctx.fillStyle = 'black';
            ctx.fillRect(0, 0, size, size);
            ctx.drawImage(img, (size - img.width) / 2, (size - img.height) / 2);

            canvas.toBlob((blob) => {
                if (!blob) {
                    reject(new Error('Could not create blob'));
                    return;
                }
                resolve(blob);
            });
        };
        img.onerror = reject;
        img.src = dataURL;
    });
}

function Wrapped() {
    const loading = useContext(LoadingContext);
    const query = useContext(QueryContext);
    let fileInput: HTMLInputElement | undefined;

    const handleFileChange = async () => {
        const files = fileInput?.files;
        if (!files) {
            return;
        }

        loading.setLoading(true);
        try {
            for (let i = 0; i < files.length; i++) {
                const file = files.item(i)!;
                const [prepared] = await timed(
                    () =>
                        fetchAndValidate(uploadResultSchema, () =>
                            fetch('/api/v1/changeProfilePicture', {
                                method: 'POST',
                                headers: {
                                    'Content-Type': 'application/json',
                                },
                                body: JSON.stringify({
                                    fileName: file.name + '.png',
                                }),
                            })
                        ),
                    'changeProfilePicture'
                );

                const [squared] = await timed(() => squareImage(file), 'squareImage');

                const xhr = new XMLHttpRequest();
                const [success] = await timed(
                    () =>
                        new Promise<boolean>(async (resolve) => {
                            xhr.upload.addEventListener('progress', (event) => {
                                if (event.lengthComputable) {
                                    loading.setProgress((event.loaded / event.total) * 100);
                                }
                            });
                            0;
                            xhr.addEventListener('loadend', () => {
                                resolve(xhr.readyState == 4 && xhr.status == 200);
                            });
                            xhr.open('POST', prepared.url, true);
                            xhr.setRequestHeader('Authentication', prepared.uploadToken);
                            xhr.send(squared);
                        }),
                    'upload'
                );
                if (!success) {
                    throw new Error('Failed to upload file');
                }

                query.refetch('user');
            }
        } catch (e) {
            loading.setError(String(e));
        } finally {
            loading.setLoading(false);
        }
    };
    return (
        <>
            <button class="button" type="button" onClick={() => fileInput?.click()}>
                Upload new profile picture
            </button>
            <input type="file" ref={fileInput} class="hidden" onInput={handleFileChange} />
        </>
    );
}

export default function () {
    return (
        <Loading inline={false} initial={false} progressBar={true}>
            <Wrapped />
        </Loading>
    );
}
