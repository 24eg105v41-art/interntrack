import { Link } from 'react-router-dom'
import { getRole, isLoggedIn } from '../api.js'

const features = [
  {
    icon: '01',
    title: 'Track every application',
    text: 'Log each internship with company, role, location, work mode, stipend and source link, then move it through Applied → Shortlisted → Selected.'
  },
  {
    icon: '02',
    title: 'Never miss a deadline',
    text: 'A single board shows application cut-offs and offer response windows, colour-coded as overdue, due today, due soon or upcoming.'
  },
  {
    icon: '03',
    title: 'Manage offers with confidence',
    text: 'Record offer letters, joining dates and response deadlines, then accept or decline before the window expires.'
  },
  {
    icon: '04',
    title: 'Admin visibility',
    text: 'Administrators get a live view of every student, internship, offer and upcoming deadline across the platform.'
  }
]

export default function Home() {
  const loggedIn = isLoggedIn()
  const role = getRole()

  const primary = loggedIn
    ? { to: role === 'ADMIN' ? '/admin' : '/dashboard', label: role === 'ADMIN' ? 'Open admin dashboard' : 'Go to dashboard' }
    : { to: '/register', label: 'Create free account' }

  return (
    <div>
      <section className="hero">
        <span className="badge applied" style={{ marginBottom: 14 }}>
          Internship management for students
        </span>
        <h1>
          One place to manage your <span>internship journey</span>
        </h1>
        <p>
          InternTrack keeps your applications, offers and deadlines organised so you always know what to do next —
          from the first application to the final offer letter.
        </p>
        <div className="hero-actions">
          <Link className="btn btn-primary" to={primary.to}>
            {primary.label}
          </Link>
          {!loggedIn && (
            <Link className="btn btn-ghost" to="/login">
              I already have an account
            </Link>
          )}
        </div>
      </section>

      <section className="grid grid-2">
        {features.map((f) => (
          <div className="card" key={f.icon}>
            <div className="feature-icon">{f.icon}</div>
            <h3>{f.title}</h3>
            <p className="muted" style={{ margin: 0, lineHeight: 1.6 }}>
              {f.text}
            </p>
          </div>
        ))}
      </section>

      <section className="card" style={{ marginTop: 20 }}>
        <h3>How it works</h3>
        <ol className="muted" style={{ lineHeight: 1.9, margin: 0, paddingLeft: 20 }}>
          <li>Create an account and add the internships you are applying to.</li>
          <li>Update each application status as you hear back from companies.</li>
          <li>When an offer arrives, log it and track the response deadline.</li>
          <li>Use the deadline board daily so nothing slips through the cracks.</li>
        </ol>
      </section>
    </div>
  )
}
