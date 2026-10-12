export const APPLICATION_STATUSES = [
  { value: 'APPLIED', label: 'Applied', tone: 'applied' },
  { value: 'SHORTLISTED', label: 'Shortlisted', tone: 'shortlisted' },
  { value: 'SELECTED', label: 'Selected', tone: 'selected' },
  { value: 'REJECTED', label: 'Rejected', tone: 'rejected' }
]

export const OFFER_STATUSES = [
  { value: 'PENDING', label: 'Pending', tone: 'applied' },
  { value: 'ACCEPTED', label: 'Accepted', tone: 'selected' },
  { value: 'DECLINED', label: 'Declined', tone: 'rejected' },
  { value: 'EXPIRED', label: 'Expired', tone: 'expired' }
]

export const WORK_MODES = [
  { value: 'ONSITE', label: 'On-site' },
  { value: 'HYBRID', label: 'Hybrid' },
  { value: 'REMOTE', label: 'Remote' }
]

export const URGENCY = {
  OVERDUE: { label: 'Overdue', tone: 'rejected' },
  TODAY: { label: 'Due today', tone: 'soon' },
  SOON: { label: 'Due soon', tone: 'soon' },
  UPCOMING: { label: 'Upcoming', tone: 'selected' }
}

export function statusTone(status) {
  const found = [...APPLICATION_STATUSES, ...OFFER_STATUSES].find((s) => s.value === status)
  return found ? found.tone : 'applied'
}

export function statusLabel(status) {
  const found = [...APPLICATION_STATUSES, ...OFFER_STATUSES].find((s) => s.value === status)
  return found ? found.label : status
}

export function daysText(days) {
  if (days === null || days === undefined) return ''
  if (days < 0) return `${Math.abs(days)} day(s) overdue`
  if (days === 0) return 'due today'
  if (days === 1) return '1 day left'
  return `${days} days left`
}

export const today = () => new Date().toISOString().split('T')[0]

export const currency = (value) =>
  value === null || value === undefined ? '' : `₹${Number(value).toLocaleString('en-IN')}`

export function formatDate(dateStr) {
  if (!dateStr) return ''
  const d = new Date(`${dateStr}T00:00:00`)
  if (Number.isNaN(d.getTime())) return dateStr
  return d.toLocaleDateString('en-IN', { day: 'numeric', month: 'short', year: 'numeric' })
}

export function dateParts(dateStr) {
  const d = new Date(`${dateStr}T00:00:00`)
  if (Number.isNaN(d.getTime())) return { d: '–', m: '' }
  return {
    d: String(d.getDate()),
    m: d.toLocaleDateString('en-IN', { month: 'short' })
  }
}

export const currencyOrNa = (value) => (value === null || value === undefined ? '—' : currency(value))
