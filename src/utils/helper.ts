/**
 * 返回一个 UUID
 */
export const uuid = () => {
  const a = URL.createObjectURL(new Blob())
  const s = a.toString()
  URL.revokeObjectURL(a)
  return s.substring(s.lastIndexOf('/') + 1)
}
