import { useQuery } from '@tanstack/react-query';
import { Link, useParams } from 'react-router-dom';
import { fetchTitle } from '../api/client';
import WatchPlayer from '../components/WatchPlayer';
import { shouldSkipIntroForTitle } from '../lib/preferences';

export default function WatchPage() {
  const { titleId = '' } = useParams();
  const { data, isLoading, error } = useQuery({
    queryKey: ['title', titleId],
    queryFn: () => fetchTitle(titleId),
    enabled: !!titleId,
  });

  if (isLoading) {
    return <div className="page-loading">Loading…</div>;
  }
  if (error || !data) {
    return (
      <div className="page-error">
        Title not found. <Link to="/">Back to browse</Link>
      </div>
    );
  }

  const skipIntro = shouldSkipIntroForTitle(titleId, data.introPolicy);

  return (
    <div className="watch-page">
      <Link to="/" className="back-link">
        ← Back
      </Link>
      <WatchPlayer titleId={titleId} titleName={data.name} skipIntro={skipIntro} />
    </div>
  );
}
