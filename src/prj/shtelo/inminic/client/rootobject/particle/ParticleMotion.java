package prj.shtelo.inminic.client.rootobject.particle;

class ParticleMotion {
    private double velX, velY;
    private double motionFriction;

    ParticleMotion(double velX, double velY) {
        this.velX = velX;
        this.velY = velY;
        this.motionFriction = 0.9;
    }

    ParticleMotion(double velX, double velY, double motionFriction) {
        this.velX = velX;
        this.velY = velY;
        this.motionFriction = motionFriction;
    }

    double moveX(double x) {
        velX *= motionFriction;

        return x + velX;
    }

    double moveY(double y) {
        velY *= motionFriction;

        return y + velY;
    }
}
