import type { BrowseRow } from '../api/types';
import TitleCard from './TitleCard';

interface Props {
  row: BrowseRow;
  focusedId: string | null;
  onFocusTitle: (id: string, previewUrl: string) => void;
  onBlurTitle: () => void;
}

export default function TitleRow({ row, focusedId, onFocusTitle, onBlurTitle }: Props) {
  return (
    <section className="title-row">
      <h2>{row.label}</h2>
      <div className="title-row-scroll">
        {row.titles.map((title) => (
          <TitleCard
            key={title.id}
            title={title}
            isFocused={focusedId === title.id}
            onFocus={() => onFocusTitle(title.id, title.previewManifestUrl)}
            onBlur={onBlurTitle}
          />
        ))}
      </div>
    </section>
  );
}
