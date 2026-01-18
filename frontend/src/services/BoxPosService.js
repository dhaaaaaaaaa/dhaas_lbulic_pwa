import axios from 'axios';

const API_URL = '/api/boxpos';

export default {
    getAll(page, size, sortBy, sortDir) {
        return axios.get(API_URL, {
            params: { page, size, sortBy, sortDir }
        });
    },

    create(data) {
        return axios.post(API_URL, data);
    },

    update(data) {
        return axios.put(API_URL, data);
    },

    delete(bId, bposId) {
        return axios.delete(API_URL, {
            params: {
                bId: bId,
                bposId: bposId
            }
        });
    }
};