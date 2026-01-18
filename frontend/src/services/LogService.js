import axios from 'axios';

const API_URL = '/api/log';

export default {
    getAll(page, size, sortBy, sortDir) {
        return axios.get(API_URL, {
            params: { page, size, sortBy, sortDir }
        });
    }
};