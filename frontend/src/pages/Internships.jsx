import { useCallback, useEffect, useState } from 'react'
import api from '../api.js'
import { APPLICATION_STATUSES, WORK_MODES, currencyOrNa, daysText, formatDate, today } from '../constants.js'

const blank = {
  companyName: '',
  roleTitle: '',
  location: '',
  workMode: 'ONSITE',
  stipend: '',
  applicationDate: today(),
  deadline: '',
  status: 'APPLIED',
  sourceLink: '',
  notes: ''
}

export default function Internships() {
  const [items, setItems] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [notice, setNotice] = useState('')
  const [filterStatus, setFilterStatus] = useState('')
  const [keyword, setKeyword] = useState('')
  const [showForm, setShowForm] = useState(false)
  const [editingId, setEditingId] = useState(null)
  const [form, setForm] = useState(blank)
  const [saving, setSaving] = useState(false)

  const load = useCallback(async () => {
    setLoading(true)
    setError('')
    try {
      const data = await api.internships({ status: filterStatus, keyword })
      setItems(data)
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }, [filterStatus, keyword])

  useEffect(() => {
    load()
  }, [load])

  const flash = (msg) => {
    setNotice(msg)
    setTimeout(() => setNotice(''), 3000)
  }

  const change = (e) => setForm({ ...form, [e.target.name]: e.target.value })

  const openAdd = () => {
    setForm(blank)
    setEditingId(null)
    setShowForm(true)
  }

  const openEdit = (item) => {
    setForm({
      companyName: item.companyName,
      roleTitle: item.roleTitle,
      location: item.location || '',
      workMode: item.workMode || 'ONSITE',
      stipend: item.stipend ?? '',
      applicationDate: item.applicationDate || today(),
      deadline: item.deadline || '',
      status: item.status,
      sourceLink: item.sourceLink || '',
      notes: item.notes || ''
    })
    setEditingId(item.internshipId)
    setShowForm(true)
  }

  const submit = async (e) => {
    e.preventDefault()
    setSaving(true)
    setError('')
    try {
      const payload = { ...form, stipend: form.stipend === '' ? 0 : Number(form.stipend) }
      if (editingId) {
        await api.updateInternship(editingId, payload)
        flash('Internship updated.')
      } else {
        await api.createInternship(payload)
        flash('Internship added.')
      }
      setShowForm(false)
      setForm(blank)
      setEditingId(null)
      load()
    } catch (err) {
      setError(err.message)
    } finally {
      setSaving(false)
    }
  }

  const changeStatus = async (item, status) => {
    try {
      await api.updateInternshipStatus(item.internshipId, status)
      load()
    } catch (err) {
      setError(err.message)
    }
  }

  const remove = async (item) => {
    if (!window.confirm(`Delete the ${item.companyName} application?`)) return
    try {
      await api.deleteInternship(item.internshipId)
      flash('Internship deleted.')
      load()
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <div>
      <div className="page-head">
        <div>
          <h1>Internships</h1>
          <p>Track each application and move it through the hiring stages.</p>
        </div>
        <button className="btn btn-primary" onClick={openAdd}>
          + Add internship
        </button>
      </div>

      {error && <div className="alert alert-error">{error}</div>}
      {notice && <div className="alert alert-success">{notice}</div>}

      {showForm && (
        <div className="card" style={{ marginBottom: 20 }}>
          <h3>{editingId ? 'Edit internship' : 'New internship'}</h3>
          <form className="form" onSubmit={submit}>
            <div className="form-row">
              <div className="field">
                <label>Company</label>
                <input name="companyName" value={form.companyName} onChange={change} required />
              </div>
              <div className="field">
                <label>Role</label>
                <input name="roleTitle" value={form.roleTitle} onChange={change} required />
              </div>
            </div>
            <div className="form-row">
              <div className="field">
                <label>Location</label>
                <input name="location" value={form.location} onChange={change} />
              </div>
              <div className="field">
                <label>Work mode</label>
                <select name="workMode" value={form.workMode} onChange={change}>
                  {WORK_MODES.map((m) => (
                    <option key={m.value} value={m.value}>
                      {m.label}
                    </option>
                  ))}
                </select>
              </div>
            </div>
            <div className="form-row">
              <div className="field">
                <label>Stipend (₹ / month)</label>
                <input name="stipend" type="number" min="0" value={form.stipend} onChange={change} required />
              </div>
              <div className="field">
                <label>Status</label>
                <select name="status" value={form.status} onChange={change}>
                  {APPLICATION_STATUSES.map((s) => (
                    <option key={s.value} value={s.value}>
                      {s.label}
                    </option>
                  ))}
                </select>
              </div>
            </div>
            <div className="form-row">
              <div className="field">
                <label>Applied on</label>
                <input name="applicationDate" type="date" value={form.applicationDate} onChange={change} />
              </div>
              <div className="field">
                <label>Deadline</label>
                <input name="deadline" type="date" value={form.deadline} onChange={change} required />
              </div>
            </div>
            <div className="field">
              <label>Source link</label>
              <input name="sourceLink" value={form.sourceLink} onChange={change} placeholder="https://…" />
            </div>
            <div className="field">
              <label>Notes</label>
              <textarea name="notes" value={form.notes} onChange={change} />
            </div>
            <div className="form-actions">
              <button
                type="button"
                className="btn btn-ghost"
                onClick={() => {
                  setShowForm(false)
                  setEditingId(null)
                }}
              >
                Cancel
              </button>
              <button type="submit" className="btn btn-primary" disabled={saving}>
                {saving ? 'Saving…' : editingId ? 'Save changes' : 'Add internship'}
              </button>
            </div>
          </form>
        </div>
      )}

      <div className="toolbar">
        <input
          placeholder="Search company or role…"
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
        />
        <select value={filterStatus} onChange={(e) => setFilterStatus(e.target.value)}>
          <option value="">All statuses</option>
          {APPLICATION_STATUSES.map((s) => (
            <option key={s.value} value={s.value}>
              {s.label}
            </option>
          ))}
        </select>
      </div>

      {loading ? (
        <div className="alert alert-info">Loading…</div>
      ) : items.length === 0 ? (
        <div className="empty">No internships found. Add one to get started.</div>
      ) : (
        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                <th>Company / Role</th>
                <th>Mode</th>
                <th>Stipend</th>
                <th>Deadline</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {items.map((i) => (
                <tr key={i.internshipId}>
                  <td>
                    <div className="cell-title">{i.companyName}</div>
                    <div className="cell-sub">{i.roleTitle}</div>
                    {i.location && <div className="cell-sub">{i.location}</div>}
                  </td>
                  <td>{i.workModeLabel}</td>
                  <td>{currencyOrNa(i.stipend)}</td>
                  <td>
                    <div>{formatDate(i.deadline)}</div>
                    <div className="cell-sub" style={{ color: i.overdue ? 'var(--danger)' : undefined }}>
                      {daysText(i.daysLeft)}
                    </div>
                  </td>
                  <td>
                    <select
                      value={i.status}
                      onChange={(e) => changeStatus(i, e.target.value)}
                      className="badge"
                      style={{ fontWeight: 600, paddingRight: 8 }}
                    >
                      {APPLICATION_STATUSES.map((s) => (
                        <option key={s.value} value={s.value}>
                          {s.label}
                        </option>
                      ))}
                    </select>
                    {i.hasOffer && (
                      <div className="cell-sub" style={{ marginTop: 4 }}>
                        Offer linked
                      </div>
                    )}
                  </td>
                  <td>
                    <div className="row-actions">
                      <button className="btn btn-sm btn-ghost" onClick={() => openEdit(i)}>
                        Edit
                      </button>
                      <button className="btn btn-sm btn-danger" onClick={() => remove(i)}>
                        Delete
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  )
}
