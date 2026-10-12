const BASE = '/api'

function getToken() {
  return localStorage.getItem('it_token') || ''
}

function setAuth(token, role, user) {
  localStorage.setItem('it_token', token)
  localStorage.setItem('it_role', role)
  if (user) localStorage.setItem('it_user', JSON.stringify(user))
}

function clearAuth() {
  localStorage.removeItem('it_token')
  localStorage.removeItem('it_role')
  localStorage.removeItem('it_user')
}

export function isLoggedIn() {
  return !!localStorage.getItem('it_token')
}

export function getRole() {
  return localStorage.getItem('it_role') || ''
}

export function getUser() {
  try {
    return JSON.parse(localStorage.getItem('it_user') || 'null')
  } catch {
    return null
  }
}

export { clearAuth }

async function request(path, { method = 'GET', body, auth = true } = {}) {
  const headers = { 'Content-Type': 'application/json' }
  if (auth) {
    const token = getToken()
    if (token) headers.Authorization = `Bearer ${token}`
  }
  const res = await fetch(`${BASE}${path}`, {
    method,
    headers,
    body: body ? JSON.stringify(body) : undefined
  })
  const text = await res.text()
  let data = {}
  try {
    data = text ? JSON.parse(text) : {}
  } catch {
    data = {}
  }
  if (!res.ok) {
    if (res.status === 401) clearAuth()
    throw new Error(data.message || `Request failed (${res.status})`)
  }
  return data
}

export const api = {
  // auth
  register: (body) => request('/auth/register', { method: 'POST', body, auth: false }),
  login: (body) => request('/auth/login', { method: 'POST', body, auth: false }),
  me: () => request('/auth/me'),
  registerAndStore: async (body) => {
    const data = await api.register(body)
    setAuth(data.token, data.role, data.user)
    return data
  },
  loginAndStore: async (body) => {
    const data = await api.login(body)
    setAuth(data.token, data.role, data.user)
    return data
  },

  // internships
  internships: (params = {}) => {
    const q = new URLSearchParams(
      Object.fromEntries(Object.entries(params).filter(([, v]) => v !== '' && v != null))
    ).toString()
    return request(`/internships${q ? `?${q}` : ''}`)
  },
  internship: (id) => request(`/internships/${id}`),
  createInternship: (body) => request('/internships', { method: 'POST', body }),
  updateInternship: (id, body) => request(`/internships/${id}`, { method: 'PUT', body }),
  updateInternshipStatus: (id, status) =>
    request(`/internships/${id}/status`, { method: 'PATCH', body: { status } }),
  deleteInternship: (id) => request(`/internships/${id}`, { method: 'DELETE' }),

  // offers
  offers: () => request('/offers'),
  createOffer: (body) => request('/offers', { method: 'POST', body }),
  updateOffer: (id, body) => request(`/offers/${id}`, { method: 'PUT', body }),
  deleteOffer: (id) => request(`/offers/${id}`, { method: 'DELETE' }),
  acceptOffer: (id) => request(`/offers/${id}/accept`, { method: 'POST' }),
  declineOffer: (id) => request(`/offers/${id}/decline`, { method: 'POST' }),

  // deadlines
  deadlines: () => request('/deadlines'),

  // admin
  adminStats: () => request('/admin/stats'),
  adminStudents: () => request('/admin/students'),
  adminInternships: (params = {}) => {
    const q = new URLSearchParams(
      Object.fromEntries(Object.entries(params).filter(([, v]) => v !== '' && v != null))
    ).toString()
    return request(`/admin/internships${q ? `?${q}` : ''}`)
  },
  adminOffers: () => request('/admin/offers'),
  adminDeadlines: () => request('/admin/deadlines')
}

export default api
