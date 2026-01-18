import axios from 'axios';

// Relativer Pfad -> Nginx kümmert sich um die Weiterleitung
const API_URL = '/api/analysis';

class AnalysisService {

    getAll(page, size, sortBy, sortOrder) {
        return axios.get(API_URL, {
            params: {
                page: page,
                size: size,
                sortBy: sortBy,     // z.B. 'pol'
                sortDir: sortOrder  // 'asc' oder 'desc'
            }
        });
    }

    get(id) {
        return axios.get(`${API_URL}/${id}`);
    }

    create(data) {
        return axios.post(API_URL, data);
    }

    update(id, data) {
        return axios.put(`${API_URL}/${id}`, data);
    }

    delete(id) {
        return axios.delete(`${API_URL}/${id}`);
    }
}

export default new AnalysisService();