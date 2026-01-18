import axios from 'axios';

const API_URL = '/api/box';

class BoxService {
    getAll(page, size, sortBy, sortOrder) {
        return axios.get(API_URL, {
            params: { page, size, sortBy, sortDir: sortOrder }
        });
    }
    create(data) { return axios.post(API_URL, data); }
    update(id, data) { return axios.put(`${API_URL}/${id}`, data); }
    delete(id) { return axios.delete(`${API_URL}/${id}`); }
}

export default new BoxService();