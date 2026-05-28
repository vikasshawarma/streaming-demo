import { Link } from 'react-router-dom';
import type { TitleSummary } from '../api/types';

interface Props {
  title: TitleSummary;
  isFocused: boolean;
  onFocus: () => void;
  onBlur: () => void;
}

export default function TitleCard({ title, isFocused, onFocus, onBlur }: Props) {
  return (
    <article
      className={`title-card ${isFocused ? 'focused' : ''}`}
      onMouseEnter={onFocus}
      onMouseLeave={onBlur}
    >
      <Link to={`/watch/${title.id}`}>
        <img src={title.posterUrl} alt={title.name} loading="lazy" />
        <span className="title-card-name">{title.name}</span>
      </Link>
    </article>
  );
}
