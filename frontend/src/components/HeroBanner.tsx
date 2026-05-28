import { Link } from 'react-router-dom';
import type { TitleSummary } from '../api/types';

interface Props {
  hero: TitleSummary;
  videoRef: React.RefObject<HTMLVideoElement>;
  onFocus: () => void;
  onBlur: () => void;
}

export default function HeroBanner({ hero, videoRef, onFocus, onBlur }: Props) {
  return (
    <section
      className="hero"
      onMouseEnter={onFocus}
      onMouseLeave={onBlur}
    >
      <video ref={videoRef} className="hero-video" muted playsInline loop />
      <div className="hero-gradient" />
      <div className="hero-content">
        <h1>{hero.name}</h1>
        <p>{hero.description}</p>
        <div className="hero-actions">
          <Link to={`/watch/${hero.id}`} className="btn-primary">
            Play
          </Link>
          <span className="runtime">{Math.floor(hero.runtimeSec / 60)} min</span>
        </div>
      </div>
    </section>
  );
}
