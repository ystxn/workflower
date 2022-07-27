import { editor, Uri } from 'monaco-editor';
import { setDiagnosticsOptions } from 'monaco-yaml';

const modelUri = Uri.parse('a://b/foo.yaml');
setDiagnosticsOptions({
    enableSchemaRequest: true,
    hover: true,
    completion: true,
    validate: true,
    format: true,
    schemas: [
        {
            // Id of the first schema
            uri: 'http://myserver/foo-schema.json',
            // Associate with our model
            fileMatch: [String(modelUri)],
            schema: {
                type: 'object',
                properties: {
                    p1: {
                        enum: ['v1', 'v2'],
                    },
                    p2: {
                        // Reference the second schema
                        $ref: 'http://myserver/bar-schema.json',
                    },
                },
            },
        },
        {
            // Id of the first schema
            uri: 'http://myserver/bar-schema.json',
            schema: {
                type: 'object',
                properties: {
                    q1: {
                        enum: ['x1', 'x2'],
                    },
                },
            },
        },
    ],
});

editor.create(document.createElement('editor'), {
    // Monaco-yaml features should just work if the editor language is set to 'yaml'.
    language: 'yaml',
    model: editor.createModel('p1: \n', 'yaml', modelUri),
});
