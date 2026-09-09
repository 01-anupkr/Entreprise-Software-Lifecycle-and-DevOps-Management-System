const state = { view: 'overview', resource: 'users', records: [], editingId: null, auth: localStorage.getItem('neuroforgeAuth') || '' };
const $ = (selector) => document.querySelector(selector);
const esc = (value) => String(value ?? '').replace(/[&<>"']/g, (char) => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#039;' }[char]));

async function api(path, options = {}) {
  const headers = { ...(options.body ? { 'Content-Type': 'application/json' } : {}), ...(state.auth ? { Authorization: `Basic ${state.auth}` } : {}), ...(options.headers || {}) };
  const response = await fetch(path, { ...options, headers });
  if (response.status === 401) { showLogin(); throw new Error('Authentication required'); }
  if (!response.ok && response.status !== 204) throw new Error(await response.text() || `Request failed (${response.status})`);
  return response.status === 204 ? null : response.json();
}

function showLogin() { $('#login-view').classList.remove('hidden'); $('#app-view').classList.add('hidden'); $('#connection-label').textContent = 'Sign in required'; }
function showApp() { $('#login-view').classList.add('hidden'); $('#app-view').classList.remove('hidden'); }
function notify(message) { const toast = $('#toast'); toast.textContent = message; toast.classList.add('show'); setTimeout(() => toast.classList.remove('show'), 2800); }
function setView(view) { state.view = view; document.querySelectorAll('.nav-item').forEach((item) => item.classList.toggle('active', item.dataset.view === view)); $('#overview-panel').classList.toggle('hidden', view !== 'overview'); $('#resource-panel').classList.toggle('hidden', view === 'overview'); $('#page-title').textContent = view[0].toUpperCase() + view.slice(1); if (view !== 'overview') { state.resource = view; loadRecords(); } }

async function loadOverview() {
  try {
    const [users, projects] = await Promise.all([api('/api/users'), api('/api/projects')]);
    $('#users-count').textContent = users.length; $('#projects-count').textContent = projects.length; $('#api-status').textContent = 'ONLINE'; $('#api-detail').textContent = 'Spring Boot / MySQL'; $('#access-level').textContent = 'AUTHENTICATED'; $('#connection-label').textContent = 'Connected to MySQL';
  } catch (error) { $('#api-status').textContent = 'OFFLINE'; $('#api-detail').textContent = error.message; }
}

async function loadRecords() {
  $('#table-wrap').innerHTML = '<div class="loading">Loading records...</div>';
  try { state.records = await api(`/api/${state.resource}`); renderTable(); } catch (error) { $('#table-wrap').innerHTML = `<div class="loading">${esc(error.message)}</div>`; }
}
function renderTable() {
  const query = $('#search-input').value.toLowerCase();
  const rows = state.records.filter((record) => JSON.stringify(record).toLowerCase().includes(query));
  $('#record-count').textContent = `${rows.length} record${rows.length === 1 ? '' : 's'}`;
  if (!rows.length) { $('#table-wrap').innerHTML = '<div class="loading">No records found.</div>'; return; }
  const isUser = state.resource === 'users';
  const head = isUser ? '<th>ID</th><th>NAME</th><th>EMAIL</th><th>ROLE</th><th>ACTIONS</th>' : '<th>ID</th><th>PROJECT</th><th>DESCRIPTION</th><th>ACTIONS</th>';
  const body = rows.map((record) => isUser ? `<tr><td>${esc(record.userId)}</td><td><strong>${esc(record.name)}</strong></td><td>${esc(record.email)}</td><td><span class="role">${esc(record.role || 'USER')}</span></td><td>${actions(record.userId)}</td></tr>` : `<tr><td>${esc(record.projectId)}</td><td><strong>${esc(record.projectName)}</strong></td><td>${esc(record.description)}</td><td>${actions(record.projectId)}</td></tr>`).join('');
  $('#table-wrap').innerHTML = `<table class="data-table"><thead><tr>${head}</tr></thead><tbody>${body}</tbody></table>`;
}
function actions(id) { return `<div class="actions"><button class="table-action" data-edit="${id}">Edit</button><button class="table-action delete" data-delete="${id}">Delete</button></div>`; }

function openModal(id = null) {
  state.editingId = id; const isUser = state.resource === 'users'; const record = state.records.find((item) => (isUser ? item.userId : item.projectId) === id) || {};
  $('#modal-kicker').textContent = id ? 'EDIT RECORD' : 'NEW RECORD'; $('#modal-title').textContent = `${id ? 'Edit' : 'Create'} ${isUser ? 'user' : 'project'}`; $('#user-fields').classList.toggle('hidden', !isUser); $('#project-fields').classList.toggle('hidden', isUser); $('#form-error').textContent = '';
  const form = $('#record-form'); form.reset(); if (isUser) { form.name.value = record.name || ''; form.email.value = record.email || ''; form.password.value = id ? '' : ''; form.password.required = !id; form.role.value = record.role || 'USER'; } else { form.projectName.value = record.projectName || ''; form.description.value = record.description || ''; }
  $('#modal-backdrop').classList.remove('hidden');
}
function closeModal() { $('#modal-backdrop').classList.add('hidden'); state.editingId = null; }
async function saveRecord(event) { event.preventDefault(); const form = event.currentTarget; const isUser = state.resource === 'users'; const data = isUser ? { name: form.name.value, email: form.email.value, role: form.role.value } : { projectName: form.projectName.value, description: form.description.value }; if (isUser && form.password.value) data.password = form.password.value; const method = state.editingId ? (Object.keys(data).length < (isUser ? 4 : 2) ? 'PATCH' : 'PUT') : 'POST'; try { await api(`/api/${state.resource}${state.editingId ? `/${state.editingId}` : ''}`, { method, body: JSON.stringify(data) }); closeModal(); notify(`${isUser ? 'User' : 'Project'} saved successfully`); await loadRecords(); loadOverview(); } catch (error) { $('#form-error').textContent = error.message; } }
async function deleteRecord(id) { if (!confirm('Delete this record? This cannot be undone.')) return; try { await api(`/api/${state.resource}/${id}`, { method: 'DELETE' }); notify('Record deleted'); await loadRecords(); loadOverview(); } catch (error) { notify(error.message); } }

$('#login-form').addEventListener('submit', async (event) => { event.preventDefault(); const raw = `${$('#login-email').value}:${$('#login-password').value}`; state.auth = btoa(raw); try { await api('/api/users'); localStorage.setItem('neuroforgeAuth', state.auth); showApp(); loadOverview(); notify('Signed in successfully'); } catch (error) { state.auth = ''; $('#login-error').textContent = 'Unable to sign in. Check your email and password.'; } });
document.querySelectorAll('[data-view]').forEach((button) => button.addEventListener('click', () => setView(button.dataset.view)));
$('#refresh-btn').addEventListener('click', () => state.view === 'overview' ? loadOverview() : loadRecords()); $('#open-docs').addEventListener('click', () => window.open('/swagger-ui/index.html', '_blank')); $('#new-record').addEventListener('click', () => openModal()); $('#modal-close').addEventListener('click', closeModal); $('#modal-cancel').addEventListener('click', closeModal); $('#record-form').addEventListener('submit', saveRecord); $('#search-input').addEventListener('input', renderTable); $('#profile-btn').addEventListener('click', () => { localStorage.removeItem('neuroforgeAuth'); state.auth = ''; showLogin(); });
$('#table-wrap').addEventListener('click', (event) => { const edit = event.target.closest('[data-edit]'); const remove = event.target.closest('[data-delete]'); if (edit) openModal(Number(edit.dataset.edit)); if (remove) deleteRecord(Number(remove.dataset.delete)); });

(async function init() { try { await api('/'); showApp(); loadOverview(); } catch { showLogin(); } })();
