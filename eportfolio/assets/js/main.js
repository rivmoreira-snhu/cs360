
(function(){
  const toggle = () => {
    const current = document.documentElement.getAttribute('data-theme');
    const next = current === 'dark' ? 'light' : 'dark';
    document.documentElement.setAttribute('data-theme', next);
    localStorage.setItem('theme', next);
  };
  const saved = localStorage.getItem('theme');
  if(saved){
    document.documentElement.setAttribute('data-theme', saved);
  }
  document.addEventListener('DOMContentLoaded', function(){
    const btn = document.getElementById('theme-toggle');
    if(btn){ btn.addEventListener('click', toggle); }
  });
})();
