(function flexible(window, document) {
  function resetFontSize() {
    const size = (document.documentElement.clientWidth / 750) * 37.5;
    document.documentElement.style.fontSize = size + 'px';
  }

  // reset root font size on page show or resize
  window.addEventListener('pageshow', resetFontSize);
  window.addEventListener('resize', resetFontSize);
})(window, document);
