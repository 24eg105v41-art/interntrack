import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import api, { getUser } from '../api.js'
import {
  APPLICATION_STATUSES,
  URGENCY,
  currencyOrNa,
  dateParts,
  daysText,
  formatDate,
  statusTone
} from '../constants.js'

export default function Dashboard() {
  const user = getUser()
  const [internships, setInternships] = useState([])
  const [offers, setOffers] = useState([])
  const [deadlines, setDeadlines] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    let active = true
    Promise.all([api.internships(), api.offers(), api.deadlines()])
      .then(([i, o, d]) => {
        if (!active) return
        setInternships(i)
        setOffers(o)
        setDeadlines(d)
      })
      .catch((err) => active && setError(err.message))
      .finally(() => active && setLoading(false))
    return () => {
      active = false
    }
  }, [])

  const active = internships.filter((i) => i.status === 'APPLIED' || i.status === 'SHORTLISTED')
  const selected = internships.filter((i) => i.status === 'SELECTED')
  const pendingOffers = offers.filter((o) => o.status === 'PENDING')
  const upcoming = deadlines.slice(0, 5)
  const recent = [...internships].slice(0, 5)

  const pipeline = APPLICATION_STATUSES.map((s) => ({
    ...s,
    count: internships.filter((i) => i.status === s.value).length
  }))

  return (
    <div>
      <div className="page-head">
        <div>
          <h1>Hi {user?.name?.split(' ')[0] || 'there'} 👋</h1>
          <p>Here is the state of your internship search today.</p>
        </div>
        <div className="row-actions">
          <Link className="btn btn-primary" to="/internships">
            Add internship
          </Link>
          <Link className="btn btn-ghost" to="/deadlines">
            Deadline board
          </Link>
        </div>
      </div>

      {error && <div className="alert alert-error">{error}</div>}
      {loading && <div className="alert alert-info">Loading your dashboard…</div>}

      {!loading && (
        <>
          <section className="grid grid-4" style={{ marginBottom: 20 }}>
            <div className="stat">
              <div className="stat-label">Applications</div>
              <div className="stat-value">{internships.length}</div>
            </div>
            <div className="stat">
              <div className="stat-label">In progress</div>
              <div className="stat-value">{active.length}</div>
            </div>
            <div className="stat">
              <div className="stat-label">Selected</div>
              <div className="stat-value">{selected.length}</div>
            </div>
            <div className="stat">
              <div className="stat-label">Pending offers</div>
              <div className="stat-value">{pendingOffers.length}</div>
            </div>
          </section>

          <section className="card" style={{ marginBottom: 20 }}>
            <h3>Application pipeline</h3>
            <div className="pipeline">
              {pipeline.map((p) => (
                <div key={p.value} className="stat" style={{ boxShadow: 'none' }}>
                  <div className="stat-label">{p.label}</div>
                  <div className="stat-value">{p.count}</div>
                </div>
              ))}
            </div>
          </section>

          <div className="grid grid-2">
            <section className="card">
              <div className="page-head" style={{ marginBottom: 12 }}>
                <h3 style={{ margin: 0 }}>Next deadlines</h3>
                <Link className="btn btn-sm btn-ghost" to="/deadlines">
                  View all
                </Link>
              </div>
              {upcoming.length === 0 ? (
                <p className="muted" style={{ margin: 0 }}>
                  No deadlines yet. Add an internship or offer to see them here.
                </p>
              ) : (
                upcoming.map((d) => {
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
                        <div className="s">{daysText(d.daysLeft)}</div>
                      </div>
                      <span className={`badge ${u.tone}`}>{u.label}</span>
                    </div>
                  )
                })
              )}
            </section>

            <section className="card">
              <div className="page-head" style={{ marginBottom: 12 }}>
                <h3 style={{ margin: 0 }}>Recent applications</h3>
                <Link className="btn btn-sm btn-ghost" to="/internships">
                  Manage
                </Link>
              </div>
              {recent.length === 0 ? (
                <p className="muted" style={{ margin: 0 }}>
                  You have not added any internships yet.
                </p>
              ) : (
                recent.map((i) => (
                  <div className="deadline-item" key={i.internshipId}>
                    <div className="deadline-body">
                      <div className="t">{i.companyName}</div>
                      <div className="s">
                        {i.roleTitle} · {i.workModeLabel} · {currencyOrNa(i.stipend)}
                      </div>
                      <div className="s">Deadline: {formatDate(i.deadline)}</div>
                    </div>
                    <span className={`badge ${statusTone(i.status)}`}>{i.statusLabel}</span>
                  </div>
                ))
              )}
            </section>
          </div>
        </>
      )}
    </div>
  )
}
