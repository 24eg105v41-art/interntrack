import { useEffect, useState } from 'react'
import api from '../api.js'
import { OFFER_STATUSES, currencyOrNa, daysText, formatDate, statusTone, today } from '../constants.js'

const blank = {
  internshipId: '',
  companyName: '',
  roleTitle: '',
  stipend: '',
  offerDate: today(),
  responseDeadline: '',
  joiningDate: '',
  letterLink: '',
  notes: ''
}

export default function Offers() {
  const [offers, setOffers] = useState([])
  const [internships, setInternships] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [notice, setNotice] = useState('')
  const [showForm, setShowForm] = useState(false)
  const [editingId, setEditingId] = useState(null)
  const [form, setForm] = useState(blank)
  const [saving, setSaving] = useState(false)

  const load = async () => {
    setLoading(true)
    setError('')
    try {
      const [o, i] = await Promise.all([api.offers(), api.internships()])
      setOffers(o)
      setInternships(i)
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    load()
  }, [])

  const flash = (msg) => {
    setNotice(msg)
    setTimeout(() => setNotice(''), 3000)
  }

  const change = (e) => setForm({ ...form, [e.target.name]: e.target.value })

  const onPickInternship = (e) => {
    const id = e.target.value
    const linked = internships.find((i) => String(i.internshipId) === String(id))
    setForm((f) => ({
      ...f,
      internshipId: id,
      companyName: linked && !f.companyName ? linked.companyName : f.companyName,
      roleTitle: linked && !f.roleTitle ? linked.roleTitle : f.roleTitle,
      stipend: linked && f.stipend === '' ? linked.stipend ?? '' : f.stipend
    }))
  }

  const openAdd = () => {
    setForm(blank)
    setEditingId(null)
    setShowForm(true)
  }

  const openEdit = (o) => {
    setForm({
      internshipId: o.internshipId ?? '',
      companyName: o.companyName,
      roleTitle: o.roleTitle,
      stipend: o.stipend ?? '',
      offerDate: o.offerDate || today(),
      responseDeadline: o.responseDeadline || '',
      joiningDate: o.joiningDate || '',
      letterLink: o.letterLink || '',
      notes: o.notes || ''
    })
    setEditingId(o.offerId)
    setShowForm(true)
  }

  const submit = async (e) => {
    e.preventDefault()
    setSaving(true)
    setError('')
    try {
      const payload = {
        ...form,
        internshipId: form.internshipId ? Number(form.internshipId) : null,
        stipend: form.stipend === '' ? 0 : Number(form.stipend),
        responseDeadline: form.responseDeadline || null,
        joiningDate: form.joiningDate || null
      }
      if (editingId) {
        await api.updateOffer(editingId, payload)
        flash('Offer updated.')
      } else {
        await api.createOffer(payload)
        flash('Offer recorded.')
      }
      setShowForm(false)
      setEditingId(null)
      setForm(blank)
      load()
    } catch (err) {
      setError(err.message)
    } finally {
      setSaving(false)
    }
  }

  const respond = async (offer, action) => {
    try {
      if (action === 'accept') await api.acceptOffer(offer.offerId)
      else await api.declineOffer(offer.offerId)
      flash(`Offer ${action === 'accept' ? 'accepted' : 'declined'}.`)
      load()
    } catch (err) {
      setError(err.message)
    }
  }

  const remove = async (offer) => {
    if (!window.confirm(`Delete the ${offer.companyName} offer?`)) return
    try {
      await api.deleteOffer(offer.offerId)
      flash('Offer deleted.')
      load()
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <div>
      <div className="page-head">
        <div>
          <h1>Offers</h1>
          <p>Record offer letters and respond before the deadline.</p>
        </div>
        <button className="btn btn-primary" onClick={openAdd}>
          + Record offer
        </button>
      </div>

      {error && <div className="alert alert-error">{error}</div>}
      {notice && <div className="alert alert-success">{notice}</div>}

      {showForm && (
        <div className="card" style={{ marginBottom: 20 }}>
          <h3>{editingId ? 'Edit offer' : 'New offer'}</h3>
          <form className="form" onSubmit={submit}>
            <div className="field">
              <label>Linked internship (optional)</label>
              <select name="internshipId" value={form.internshipId} onChange={onPickInternship}>
                <option value="">Not linked</option>
                {internships.map((i) => (
                  <option key={i.internshipId} value={i.internshipId}>
                    {i.companyName} — {i.roleTitle} ({i.statusLabel})
                  </option>
                ))}
              </select>
            </div>
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
            <div className="field">
              <label>Stipend (₹ / month)</label>
              <input name="stipend" type="number" min="0" value={form.stipend} onChange={change} required />
            </div>
            <div className="form-row">
              <div className="field">
                <label>Offer date</label>
                <input name="offerDate" type="date" value={form.offerDate} onChange={change} />
              </div>
              <div className="field">
                <label>Response deadline</label>
                <input
                  name="responseDeadline"
                  type="date"
                  value={form.responseDeadline}
                  onChange={change}
                />
              </div>
            </div>
            <div className="form-row">
              <div className="field">
                <label>Joining date</label>
                <input name="joiningDate" type="date" value={form.joiningDate} onChange={change} />
              </div>
              <div className="field">
                <label>Offer letter link</label>
                <input name="letterLink" value={form.letterLink} onChange={change} placeholder="https://…" />
              </div>
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
                {saving ? 'Saving…' : editingId ? 'Save changes' : 'Record offer'}
              </button>
            </div>
          </form>
        </div>
      )}

      {loading ? (
        <div className="alert alert-info">Loading…</div>
      ) : offers.length === 0 ? (
        <div className="empty">No offers recorded yet.</div>
      ) : (
        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                <th>Company / Role</th>
                <th>Stipend</th>
                <th>Response by</th>
                <th>Joining</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {offers.map((o) => (
                <tr key={o.offerId}>
                  <td>
                    <div className="cell-title">{o.companyName}</div>
                    <div className="cell-sub">{o.roleTitle}</div>
                    {o.letterLink && (
                      <a className="cell-sub" href={o.letterLink} target="_blank" rel="noreferrer">
                        Offer letter ↗
                      </a>
                    )}
                  </td>
                  <td>{currencyOrNa(o.stipend)}</td>
                  <td>
                    <div>{formatDate(o.responseDeadline)}</div>
                    {o.status === 'PENDING' && (
                      <div className="cell-sub" style={{ color: o.expired ? 'var(--danger)' : undefined }}>
                        {daysText(o.daysToRespond)}
                      </div>
                    )}
                  </td>
                  <td>{o.joiningDate ? formatDate(o.joiningDate) : '—'}</td>
                  <td>
                    <span className={`badge ${statusTone(o.status)}`}>{o.statusLabel}</span>
                  </td>
                  <td>
                    <div className="row-actions">
                      {o.status === 'PENDING' && (
                        <>
                          <button className="btn btn-sm btn-success" onClick={() => respond(o, 'accept')}>
                            Accept
                          </button>
                          <button className="btn btn-sm btn-danger" onClick={() => respond(o, 'decline')}>
                            Decline
                          </button>
                        </>
                      )}
                      <button className="btn btn-sm btn-ghost" onClick={() => openEdit(o)}>
                        Edit
                      </button>
                      <button className="btn btn-sm btn-danger" onClick={() => remove(o)}>
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
