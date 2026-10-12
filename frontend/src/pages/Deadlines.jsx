import { useEffect, useMemo, useState } from 'react'
import api from '../api.js'
import { URGENCY, dateParts, daysText } from '../constants.js'

const FILTERS = [
  { value: 'ALL', label: 'All' },
  { value: 'OVERDUE', label: 'Overdue' },
  { value: 'TODAY', label: 'Today' },
  { value: 'SOON', label: 'Due soon' },
  { value: 'UPCOMING', label: 'Upcoming' }
]

export default function Deadlines() {
  const [items, setItems] = useState([])
  const [loading, setLoading] = useState(true)
  const [filter, setFilter] = useState('ALL')
  const [error, setError] = useState('')

  useEffect(() => {
    let active = true
    api
      .deadlines()
      .then((d) => active && setItems(d))
      .catch((err) => active && setError(err.message))
      .finally(() => active && setLoading(false))
    return () => {
      active = false
    }
  }, [])

  const counts = useMemo(() => {
    const c = { OVERDUE: 0, TODAY: 0, SOON: 0, UPCOMING: 0 }
    items.forEach((i) => {
      if (c[i.urgency] !== undefined) c[i.urgency] += 1
    })
    return c
  }, [items])

  const visible = filter === 'ALL' ? items : items.filter((i) => i.urgency === filter)

  return (
    <div>
      <div className="page-head">
        <div>
          <h1>Deadline board</h1>
          <p>Application cut-offs and offer response windows in one place.</p>
        </div>
      </div>

      {error && <div className="alert alert-error">{error}</div>}

      <section className="grid grid-4" style={{ marginBottom: 20 }}>
        <div className="stat">
          <div className="stat-label" style={{ color: 'var(--danger)' }}>
            Overdue
          </div>
          <div className="stat-value">{counts.OVERDUE}</div>
        </div>
        <div className="stat">
          <div className="stat-label" style={{ color: 'var(--warn)' }}>
            Due today
          </div>
          <div className="stat-value">{counts.TODAY}</div>
        </div>
        <div className="stat">
          <div className="stat-label">Due soon</div>
          <div className="stat-value">{counts.SOON}</div>
        </div>
        <div className="stat">
          <div className="stat-label">Upcoming</div>
          <div className="stat-value">{counts.UPCOMING}</div>
        </div>
      </section>

      <div className="tabs">
        {FILTERS.map((f) => (
          <button
            key={f.value}
            className={filter === f.value ? 'active' : ''}
            onClick={() => setFilter(f.value)}
          >
            {f.label}
          </button>
        ))}
      </div>

      {loading ? (
        <div className="alert alert-info">Loading deadlines…</div>
      ) : visible.length === 0 ? (
        <div className="empty">Nothing here. Your schedule is clear.</div>
      ) : (
        visible.map((d) => {
          const u = URGENCY[d.urgency] || URGENCY.UPCOMING
          const parts = dateParts(d.dueDate)
          return (
            <div className="deadline-item" key={`${d.type}-${d.referenceId}`}>
              <div className="deadline-date">
                <div className="d">{parts.d}</div>
                <div className="m">{parts.m}</div>
              </div>
              <div className="deadline-body">
                <div className="t">{d.title}</div>
                <div className="s">{d.subtitle}</div>
                <div className="s">{daysText(d.daysLeft)}</div>
              </div>
              <div className="row-actions">
                <span className="badge applied">{d.type === 'OFFER' ? 'Offer' : 'Application'}</span>
                <span className={`badge ${u.tone}`}>{u.label}</span>
              </div>
            </div>
          )
        })
      )}
    </div>
  )
}
