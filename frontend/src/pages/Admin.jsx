import { useEffect, useState } from 'react'
import api from '../api.js'
import {
  APPLICATION_STATUSES,
  OFFER_STATUSES,
  URGENCY,
  currencyOrNa,
  dateParts,
  daysText,
  formatDate,
  statusLabel,
  statusTone
} from '../constants.js'

const TABS = [
  { value: 'overview', label: 'Overview' },
  { value: 'students', label: 'Students' },
  { value: 'internships', label: 'Internships' },
  { value: 'offers', label: 'Offers' },
  { value: 'deadlines', label: 'Deadlines' }
]

export default function Admin() {
  const [tab, setTab] = useState('overview')
  const [stats, setStats] = useState(null)
  const [students, setStudents] = useState([])
  const [internships, setInternships] = useState([])
  const [offers, setOffers] = useState([])
  const [deadlines, setDeadlines] = useState([])
  const [statusFilter, setStatusFilter] = useState('')
  const [keyword, setKeyword] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')

  useEffect(() => {
    let active = true
    setLoading(true)
    setError('')
    const tasks = {
      overview: () => api.adminStats().then((d) => active && setStats(d)),
      students: () => api.adminStudents().then((d) => active && setStudents(d)),
      internships: () =>
        api.adminInternships({ status: statusFilter, keyword }).then((d) => active && setInternships(d)),
      offers: () => api.adminOffers().then((d) => active && setOffers(d)),
      deadlines: () => api.adminDeadlines().then((d) => active && setDeadlines(d))
    }
    tasks[tab]()
      .catch((err) => active && setError(err.message))
      .finally(() => active && setLoading(false))
    return () => {
      active = false
    }
  }, [tab, statusFilter, keyword])

  return (
    <div>
      <div className="page-head">
        <div>
          <h1>Admin dashboard</h1>
          <p>Platform-wide view of students, internships, offers and deadlines.</p>
        </div>
      </div>

      <div className="tabs">
        {TABS.map((t) => (
          <button key={t.value} className={tab === t.value ? 'active' : ''} onClick={() => setTab(t.value)}>
            {t.label}
          </button>
        ))}
      </div>

      {error && <div className="alert alert-error">{error}</div>}
      {loading && <div className="alert alert-info">Loading…</div>}

      {tab === 'overview' && stats && !loading && (
        <>
          <section className="grid grid-4" style={{ marginBottom: 20 }}>
            <div className="stat">
              <div className="stat-label">Students</div>
              <div className="stat-value">{stats.totalStudents}</div>
            </div>
            <div className="stat">
              <div className="stat-label">Internships</div>
              <div className="stat-value">{stats.totalInternships}</div>
            </div>
            <div className="stat">
              <div className="stat-label">Offers</div>
              <div className="stat-value">{stats.totalOffers}</div>
            </div>
            <div className="stat">
              <div className="stat-label">Overdue deadlines</div>
              <div className="stat-value" style={{ color: stats.overdueDeadlines ? 'var(--danger)' : undefined }}>
                {stats.overdueDeadlines}
              </div>
            </div>
          </section>

          <section className="card">
            <h3>Internships by status</h3>
            <div className="pipeline">
              {APPLICATION_STATUSES.map((s) => (
                <div key={s.value} className="stat" style={{ boxShadow: 'none' }}>
                  <div className="stat-label">{s.label}</div>
                  <div className="stat-value">{stats.internshipsByStatus?.[s.value] ?? 0}</div>
                </div>
              ))}
            </div>
            <p className="muted" style={{ marginBottom: 0, marginTop: 14 }}>
              {stats.pendingOffers} offer(s) awaiting a student response.
            </p>
          </section>
        </>
      )}

      {tab === 'students' && !loading && (
        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                <th>Student</th>
                <th>Branch</th>
                <th>Year</th>
                <th>CGPA</th>
                <th>Internships</th>
                <th>Selected</th>
                <th>Offers</th>
              </tr>
            </thead>
            <tbody>
              {students.map((s) => (
                <tr key={s.userId}>
                  <td>
                    <div className="cell-title">{s.name}</div>
                    <div className="cell-sub">{s.email}</div>
                  </td>
                  <td>{s.branch || '—'}</td>
                  <td>{s.graduationYear || '—'}</td>
                  <td>{s.cgpa ?? '—'}</td>
                  <td>{s.internshipCount}</td>
                  <td>{s.selectedCount}</td>
                  <td>
                    {s.offerCount}
                    {s.pendingOffers > 0 && <span className="cell-sub"> ({s.pendingOffers} pending)</span>}
                  </td>
                </tr>
              ))}
              {students.length === 0 && (
                <tr>
                  <td colSpan={7} className="muted">
                    No students yet.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      )}

      {tab === 'internships' && (
        <>
          <div className="toolbar">
            <input
              placeholder="Search student, company or role…"
              value={keyword}
              onChange={(e) => setKeyword(e.target.value)}
            />
            <select value={statusFilter} onChange={(e) => setStatusFilter(e.target.value)}>
              <option value="">All statuses</option>
              {APPLICATION_STATUSES.map((s) => (
                <option key={s.value} value={s.value}>
                  {s.label}
                </option>
              ))}
            </select>
          </div>
          {!loading && (
            <div className="table-wrap">
              <table>
                <thead>
                  <tr>
                    <th>Student</th>
                    <th>Company / Role</th>
                    <th>Mode</th>
                    <th>Stipend</th>
                    <th>Deadline</th>
                    <th>Status</th>
                  </tr>
                </thead>
                <tbody>
                  {internships.map((i) => (
                    <tr key={i.internshipId}>
                      <td>
                        <div className="cell-title">{i.studentName || '—'}</div>
                        <div className="cell-sub">{i.studentEmail}</div>
                      </td>
                      <td>
                        <div className="cell-title">{i.companyName}</div>
                        <div className="cell-sub">{i.roleTitle}</div>
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
                        <span className={`badge ${statusTone(i.status)}`}>{i.statusLabel}</span>
                      </td>
                    </tr>
                  ))}
                  {internships.length === 0 && (
                    <tr>
                      <td colSpan={6} className="muted">
                        No internships match your filters.
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          )}
        </>
      )}

      {tab === 'offers' && !loading && (
        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                <th>Student</th>
                <th>Company / Role</th>
                <th>Stipend</th>
                <th>Response by</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {offers.map((o) => (
                <tr key={o.offerId}>
                  <td>
                    <div className="cell-title">{o.studentName || '—'}</div>
                    <div className="cell-sub">{o.studentEmail}</div>
                  </td>
                  <td>
                    <div className="cell-title">{o.companyName}</div>
                    <div className="cell-sub">{o.roleTitle}</div>
                  </td>
                  <td>{currencyOrNa(o.stipend)}</td>
                  <td>{formatDate(o.responseDeadline)}</td>
                  <td>
                    <span className={`badge ${statusTone(o.status)}`}>{statusLabel(o.status)}</span>
                  </td>
                </tr>
              ))}
              {offers.length === 0 && (
                <tr>
                  <td colSpan={5} className="muted">
                    No offers recorded yet.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      )}

      {tab === 'deadlines' && !loading && (
        <>
          {deadlines.length === 0 ? (
            <div className="empty">No upcoming deadlines.</div>
          ) : (
            deadlines.map((d) => {
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
                  <span className={`badge ${u.tone}`}>{u.label}</span>
                </div>
              )
            })
          )}
        </>
      )}
    </div>
  )
}
