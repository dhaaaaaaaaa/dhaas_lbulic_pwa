import axios from 'axios';
const API_URL = '/api/sample';

export default {
    getAll(page, size, sortBy, sortDir) {
        return axios.get(API_URL, { params: { page, size, sortBy, sortDir } });
    },
    create(data) { return axios.post(API_URL, data); },
    update(data) { return axios.put(API_URL, data); },
    delete(sId, sStamp) {
        return axios.delete(API_URL, { params: { sId, sStamp } });
    }
};