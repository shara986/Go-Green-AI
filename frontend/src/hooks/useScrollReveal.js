import { useEffect, useRef } from 'react';

/**
 * IntersectionObserver-based scroll reveal hook.
 *
 * Usage:
 *   const ref = useScrollReveal();
 *   <section ref={ref} className="scroll-reveal"> ... </section>
 *
 * Add CSS class `.scroll-reveal` with initial hidden state,
 * and `.scroll-reveal.visible` with the revealed state.
 */
const useScrollReveal = (options = {}) => {
  const ref = useRef(null);

  useEffect(() => {
    const el = ref.current;
    if (!el) return;

    const observer = new IntersectionObserver(
      ([entry]) => {
        if (entry.isIntersecting) {
          el.classList.add('visible');
          observer.unobserve(el); // trigger only once
        }
      },
      {
        threshold: options.threshold || 0.12,
        rootMargin: options.rootMargin || '0px 0px -40px 0px',
      }
    );

    observer.observe(el);

    return () => observer.disconnect();
  }, [options.threshold, options.rootMargin]);

  return ref;
};

export default useScrollReveal;
