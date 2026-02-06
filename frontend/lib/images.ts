const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

export const getImageUrl = (imageName: string | null | undefined): string => {
  if (!imageName) {
    console.warn('getImageUrl called with empty imageName');
    return '';
  }
  // Use ImageController's /images/path/{path} endpoint
  // Don't encode if it's already a full path or URL
  if (imageName.startsWith('http://') || imageName.startsWith('https://')) {
    return imageName;
  }
  // The backend expects the path as-is, but we should encode special characters
  const encoded = encodeURIComponent(imageName);
  const url = `${API_BASE_URL}/images/path/${encoded}`;
  console.log('Constructed image URL:', url, 'from name:', imageName);
  return url;
};

export const getImageUrlById = (imageId: number): string => {
  if (!imageId) return '';
  return `${API_BASE_URL}/images/${imageId}`;
};


