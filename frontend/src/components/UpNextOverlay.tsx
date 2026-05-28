import type { UpNextCandidate } from '../api/types';

interface Props {
  candidate: UpNextCandidate;
  countdown: number;
  onPlayNow: () => void;
  onCancel: () => void;
}

export default function UpNextOverlay({ candidate, countdown, onPlayNow, onCancel }: Props) {
  return (
    <div className="up-next-overlay" role="dialog" aria-label="Up next">
      <div className="up-next-card">
        <img src={candidate.posterUrl} alt="" className="up-next-poster" />
        <div className="up-next-info">
          <p className="up-next-label">Up Next in {countdown}s</p>
          <h3>{candidate.name}</h3>
          <p className="up-next-reason">{candidate.reason}</p>
          <div className="up-next-actions">
            <button type="button" className="btn-primary" onClick={onPlayNow}>
              Play Now
            </button>
            <button type="button" className="btn-ghost" onClick={onCancel}>
              Cancel
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
