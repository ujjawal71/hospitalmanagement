const API_BASE = '/api';

function getToken() {
  return sessionStorage.getItem('token');
}

function getRole() {
  return sessionStorage.getItem('role');
}

function getUserId() {
  return sessionStorage.getItem('userId');
}

function getUserName() {
  return sessionStorage.getItem('userName');
}

function setAuth(data) {
  if (data.token) sessionStorage.setItem('token', data.token);
  if (data.role) sessionStorage.setItem('role', data.role);
  if (data.userId != null) sessionStorage.setItem('userId', String(data.userId));
  if (data.name) sessionStorage.setItem('userName', data.name);
}

function clearAuth() {
  sessionStorage.removeItem('token');
  sessionStorage.removeItem('role');
  sessionStorage.removeItem('userId');
  sessionStorage.removeItem('userName');
}

function authHeaders() {
  const token = getToken();
  return {
    'Content-Type': 'application/json',
    ...(token ? { 'X-Auth-Token': token } : {}),
  };
}

async function api(method, path, body) {
  const opts = { method, headers: authHeaders() };
  if (body) opts.body = JSON.stringify(body);
  const res = await fetch(API_BASE + path, opts);
  const text = await res.text();
  let data = null;
  try {
    data = text ? JSON.parse(text) : null;
  } catch (_) {
    data = { error: text || 'Request failed' };
  }
  if (!res.ok) {
    throw new Error(data?.error || data?.message || `HTTP ${res.status}`);
  }
  return data;
}

const apiGet = (path) => api('GET', path);
const apiPost = (path, body) => api('POST', path, body);
const apiPut = (path, body) => api('PUT', path, body);
const apiDelete = (path) => api('DELETE', path);

async function login(email, password, role) {
  const res = await apiPost('/auth/login', { email, password, role });
  if (res.success) {
    setAuth({ token: res.token, role: res.role, userId: res.userId, name: res.name });
    return res;
  }
  throw new Error(res.message || 'Login failed');
}

async function register(patient) {
  return apiPost('/auth/register', patient);
}

function redirectByRole() {
  const role = getRole();
  if (role === 'ADMIN') location.href = '/pages/admin-dashboard.html';
  else if (role === 'DOCTOR') location.href = '/pages/doctor-dashboard.html';
  else if (role === 'PATIENT') location.href = '/pages/patient-dashboard.html';
  else location.href = '/index.html';
}

function requireAuth(expectedRole) {
  if (!getToken()) {
    location.href = '/index.html';
    return false;
  }
  if (expectedRole && getRole() !== expectedRole) {
    location.href = '/index.html';
    return false;
  }
  return true;
}

function formatDate(d) {
  if (!d) return '-';
  const date = new Date(d);
  return date.toLocaleDateString();
}

function formatTime(t) {
  if (!t) return '-';
  if (typeof t === 'string' && t.includes(':')) return t.substring(0, 5);
  return t;
}
