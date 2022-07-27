import React from 'react';
import ReactDOM from 'react-dom/client';
import {
    Button,
    Dropdown,
} from "@symphony-ui/uitoolkit-components/components";
import '@symphony-ui/uitoolkit-styles/dist/css/uitoolkit.css';
import './style.css';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <>
        <Dropdown
            label="Select Workflow"
            size="large"
            enableTermSearch
            onChange={() => {}}
            onTermSearch={function noRefCheck() {}}
            options={[
                { label: 'Option 1', value: '1' },
                { label: 'Option 2', value: '2' },
                { label: 'Option 3', value: '3' },
                { label: 'Option 4', value: '4' },
                { label: 'Option 5', value: '5' },
            ]}
        />
        <Button>Save Workflows</Button>
    </>
);
