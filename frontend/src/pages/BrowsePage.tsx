import { useQuery } from '@tanstack/react-query';
import { fetchBrowse } from '../api/client';
import HeroBanner from '../components/HeroBanner';
import TitleRow from '../components/TitleRow';
import { usePreviewPlayer } from '../hooks/usePreviewPlayer';

export default function BrowsePage() {
  const { data, isLoading, error } = useQuery({
    queryKey: ['browse'],
    queryFn: fetchBrowse,
  });
  const { videoRef, focusedId, focusTitle, blurTitle } = usePreviewPlayer();

  if (isLoading) {
    return <div className="page-loading">Loading catalog…</div>;
  }
  if (error || !data) {
    return <div className="page-error">Failed to load catalog. Is the API running?</div>;
  }

  return (
    <div className="browse-page">
      <header className="app-header">
        <span className="logo">STREAMING DEMO</span>
      </header>
      <HeroBanner
        hero={data.hero}
        videoRef={videoRef}
        onFocus={() => focusTitle(data.hero.id, data.hero.previewManifestUrl)}
        onBlur={blurTitle}
      />
      <main>
        {data.rows.map((row) => (
          <TitleRow
            key={row.id}
            row={row}
            focusedId={focusedId}
            onFocusTitle={focusTitle}
            onBlurTitle={blurTitle}
          />
        ))}
      </main>
    </div>
  );
}
