const Fire = function(ctx, x, y){
    const sequences = {
        idle:  { x: 0, y: 160, width: 16, height: 16, count: 3, timing: 500, loop: true }
    }

    const sprite = Sprite(ctx, x, y);
    sprite.setSequence(sequences.idle)
          .setScale(2)
          .setShadowScale({ x: 0.75, y: 0.20 })
          .useSheet("object_sprites.png");

    return {
        draw: sprite.draw,
        update: sprite.update
    };
}